package com.intellij.newCodeFormatting;

public class Wrap {
  private int myFirstEntry = -1;

  public int getFirstEntry() {
    return myFirstEntry;
  }

  public void skipFirstEntry() {
    myFirstEntry = -1;
  }

  public static class Type{
    public static final Type DO_NOT_WRAP = new Type();
    public static final Type WRAP_AS_NEEDED = new Type();
    public static final Type CHOP_IF_NEEDED = new Type();
    public static final Type WRAP_ALWAYS = new Type();
  }

  private final Type myType;

  public Wrap(final Type type) {
    myType = type;
  }

  public Type getType() {
    return myType;
  }

  public void saveFirstEntry(final int startOffset) {
    if (myFirstEntry < 0) {
      myFirstEntry = startOffset;
    }
  }

}
