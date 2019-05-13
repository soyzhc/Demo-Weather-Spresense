/*
 * Java
 *
 * Copyright 2019 Sony Corp. All rights reserved.
 * This Software has been designed by MicroEJ Corp and all rights have been transferred to Sony Corp.
 * Sony Corp. has granted MicroEJ the right to sub-licensed this Software under the enclosed license terms.
 */
package com.microej.example.hello.widget;

import java.util.Calendar;
import java.util.Date;

import com.microej.example.hello.Model;
import com.microej.example.hello.NLS;
import com.microej.example.hello.Util;
import com.microej.example.hello.style.ClassSelectors;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.widget.basic.Label;
import ej.widget.container.Dock;
import ej.widget.container.Grid;

/**
 *
 */
public class DateDetails extends CenterContainer implements Animation {

	private static final int MILIS_IN_HOUR = 3600_000;
	private final MaxSizeLabel day;
	private final Label date;
	private final Label mainTemperature;
	private final Label hour;
	private final Grid newtHours;

	public DateDetails() {
		Dock dateDock = new Dock();
		day = new MaxSizeLabel();
		day.setWords(NLS.getLocalSymbols().getWeekdays());
		day.addClassSelector(ClassSelectors.DAY);
		dateDock.setCenter(Util.addWrapper(day));
		date = new Label();
		date.addClassSelector(ClassSelectors.DATE_DETAILS);
		dateDock.addBottom(Util.addWrapper(date));
		setFirst(dateDock);

		newtHours = new Grid(true, Model.COUNT_OF_HOUR_VALUES);
		for (int i = 1; i <= Model.COUNT_OF_HOUR_VALUES; i++) {
			newtHours.add(new HourlyDetail(3 * i * MILIS_IN_HOUR));
		}
		newtHours.addClassSelector(ClassSelectors.HOURLY_TEMPERATURE);

		setLast(newtHours);
		Dock centerDock = new Dock();
		mainTemperature = new Label(
				String.valueOf(Model.getTemperature()) + NLS.getTemperatureSymbol());
		mainTemperature.addClassSelector(ClassSelectors.MAIN_TEMPERATURE);
		centerDock.setCenter(Util.addWrapper(mainTemperature));
		hour = new Label();
		hour.addClassSelector(ClassSelectors.DATE_DETAILS);
		centerDock.addBottom(Util.addWrapper(hour));
		centerDock.addClassSelector(ClassSelectors.CIRCLE);
		setCenter(centerDock);

		update();
	}

	private void update() {
		Calendar calendar = Calendar.getInstance();
		long currentTime = Model.getCurrentTime();
		calendar.setTimeInMillis(currentTime);

		Util.update(this.day, NLS.getLocalSymbols().getWeekday(calendar.get(Calendar.DAY_OF_WEEK) - 1));
		Date time = calendar.getTime();
		Util.update(this.date, NLS.getDateFormat().format(time));
		Util.update(this.hour, Util.addPadding(NLS.getFullHourFormat().format(time))
				+ NLS.getHourSymbol(calendar.get(Calendar.AM_PM)));

		for (int i = 0; i < newtHours.getWidgets().length; i++) {
			HourlyDetail widget = (HourlyDetail) newtHours.getWidgets()[i];
			widget.update(currentTime);
		}
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		update();
		return true;
	}

	@Override
	public void showNotify() {
		super.showNotify();
		ServiceLoaderFactory.getServiceLoader().getService(Animator.class).startAnimation(this);
	}

	@Override
	public void hideNotify() {
		super.hideNotify();
		ServiceLoaderFactory.getServiceLoader().getService(Animator.class).stopAnimation(this);
	}
}