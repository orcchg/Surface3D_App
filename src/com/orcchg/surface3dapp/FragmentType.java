package com.orcchg.surface3dapp;

enum FragmentType {
  FOLDERS(0), MODELS(1), INFO(2);
  private int value;
  
  FragmentType(int value) { this.value = value; }
  
  @Override
  public String toString() {
    switch (value) {
      case 0:
        return "FOLDERS";
      case 1:
        return "MODELS";
      case 2:
        return "INFO";
      default:
        return null;
    }
  }
  
  public int getValue() { return value; }
  public static FragmentType fromInt(int value) {
    switch (value) {
      case 0:
        return FOLDERS;
      case 1:
        return MODELS;
      case 2:
        return INFO;
      default:
        return null;
    }
  }
}
