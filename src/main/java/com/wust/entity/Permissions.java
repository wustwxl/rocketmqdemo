package com.wust.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Permissions {

	private static final long serialVersionUID = 1L;

	private Integer permission_id;

	private String permission_name;

}
