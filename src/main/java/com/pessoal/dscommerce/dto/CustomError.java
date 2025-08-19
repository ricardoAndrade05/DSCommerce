package com.pessoal.dscommerce.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomError {
	
	private Instant timestamp;
	private Integer status;
	private String error;
	private String path;

}
