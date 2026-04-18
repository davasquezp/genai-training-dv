package com.dv.genaitraining.queries.snapshots;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PageQuerySnapshotSupport {
  private PageQuerySnapshotSupport() {}

  public static void assertMatchesSnapshot(String actualJson, Path snapshotPath) throws Exception {
    String normalizedActual = actualJson == null ? "" : actualJson.trim();

    if (shouldUpdateSnapshots()) {
      writeSnapshot(snapshotPath, normalizedActual + "\n");
      return;
    }

    String expected = readSnapshot(snapshotPath).trim();
    JSONAssert.assertEquals(expected, normalizedActual, JSONCompareMode.STRICT);
  }

  private static boolean shouldUpdateSnapshots() {
    String prop = System.getProperty("snapshot.update", "").trim().toLowerCase();
    return prop.equals("true") || prop.equals("1") || prop.equals("yes");
  }

  private static String readSnapshot(Path snapshotPath) throws IOException {
    return Files.readString(snapshotPath, StandardCharsets.UTF_8);
  }

  private static void writeSnapshot(Path snapshotPath, String contents) throws IOException {
    Files.createDirectories(snapshotPath.getParent());
    Files.writeString(snapshotPath, contents, StandardCharsets.UTF_8);
  }
}

