package com.yuanzhi.gateway.constant;


public enum UserTypeEnum implements BaseEnum<Byte> {
  /**
   *
   */
  USER("normal user", (byte) 0),
  ADMIN("administrator", (byte) 1),
  ;

  private String desc;
  private Byte code;

  UserTypeEnum(String desc, Byte code) {
    this.desc = desc;
    this.code = code;
  }

  @Override
  public Byte getCode() {
    return this.code;
  }

  @Override
  public String getDesc() {
    return this.desc;
  }

  public String getKey() {
    return null;
  }


}
