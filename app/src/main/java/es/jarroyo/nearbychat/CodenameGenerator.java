package es.jarroyo.nearbychat;

import java.util.Random;

/** Utility class to generate random Android names */
public final class CodenameGenerator {
  private static final String[] COLORS =
      new String[] {
        "Ciconia",
        "Cuaji",
        "Naufrago",
        "Jipi",
        "Sixto",
        "Empresaurio",
        "Baronesa",
        "Durruti",
        "Papi",
        "Tinov",
        "Junior",
        "Biberon",
        "Eliot",
      };

  private static final String[] TREATS =
      new String[] {
        "Ness",
        "Milenario",
        "Echenike",
        "Tissen",
        "Pigdemont",
        "Errejon",
        "Suasso",
        "Vikingan",
        "Foforow",
        "Baba",
        "Libretitas"
      };

  private static final Random generator = new Random();

  private CodenameGenerator() {}

  /** Generate a random Android agent codename */
  public static String generate() {
    String color = COLORS[generator.nextInt(COLORS.length)];
    String treat = TREATS[generator.nextInt(TREATS.length)];
    return color + " " + treat;
  }
}
