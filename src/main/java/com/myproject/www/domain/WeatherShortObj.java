package com.myproject.www.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WeatherShortObj {
	private String baseDate;
	private String tmn;
	private String tmx;
	private String sky;
	private String pty;
	private String pop;
	private String reh;
}
