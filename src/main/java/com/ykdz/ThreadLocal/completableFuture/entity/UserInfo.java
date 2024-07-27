package com.ykdz.ThreadLocal.completableFuture.entity;

import lombok.Data;

@Data
public class UserInfo {
  private Integer id;
  private String name;
  private Integer age;

  public UserInfo() {
  }

  public UserInfo(Integer id, String name, Integer age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }
}
