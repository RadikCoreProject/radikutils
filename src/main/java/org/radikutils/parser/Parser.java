package org.radikutils.parser;

public interface Parser {
   boolean parseMap();
   boolean parseList();
   void print(boolean list);
}
