package com.ykdz.ThreadLocal.completableFuture.entity;

import lombok.Data;

@Data
public class MedalInfo {
  private Integer id;
  private String description;

  public MedalInfo() {
  }

  public MedalInfo(Integer id, String description) {
    this.id = id;
    this.description = description;
  }
}
