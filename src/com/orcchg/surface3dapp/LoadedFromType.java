package com.orcchg.surface3dapp;

enum LoadedFromType {
  ASSETS(0), FILESYSTEM(1);
  private int value;
  
  LoadedFromType(int value) { this.value = value; }
  
  @Override
  public String toString() {
    switch (value) {
      case 0:
        return "ASSETS";
      case 1:
        return "FILESYSTEM";
      default:
        return null;
    }
  }
  
  public int getValue() { return value; }
  public static LoadedFromType fromInt(int value) {
    switch (value) {
      case 0:
        return ASSETS;
      case 1:
        return FILESYSTEM;
      default:
        return null;
    }
  }
}
