import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.IOUtils;

/**
 * This script adds any language keys not in a .lang file but in en_US to that file so that all lang files have
 * every key.
 *
 * @author temp1011
 */
public class LangScript {

	private static File langFolder;
	private static File[] langFiles;
	private static Map<String, String> masterLangMap;

	/**
	 * @param args all optional: 0 - modid 1 - `master` lang file to fill from, 2 - lang file to fill to (else all)
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please supply correct arguments");
			System.exit(1);
		}

		File homeFolder = new File("");
		System.out.printf("Project root is: %s\n", homeFolder.getAbsolutePath());
		langFolder = new File(homeFolder.getAbsolutePath(), "src/main/resources/assets/" + args[0] + "/lang");
		if (!langFolder.exists() || !langFolder.isDirectory()) {
			throw new RuntimeException("Lang folder not found");
		}
		System.out.printf("Lang folder found at: %s\n", langFolder.getAbsolutePath());
		langFiles = langFolder.listFiles();

		String masterFilename = args.length == 1 ? "en_US.lang" : args[1]+".lang";
		File masterFile = findFile(masterFilename);
		masterLangMap = buildMap(masterFile);

		Collection<File> toMap = args.length > 1 ? Collections.singleton(findFile(args[2]+".lang")) : Arrays.asList(langFiles);
		toMap.remove(masterFile);

		Map<File, Set<String>> fileLangMap = new HashMap<>();
		toMap.forEach(file -> fileLangMap.put(file, getKeys(file)));
		for (Map.Entry<File, Set<String>> entry : fileLangMap.entrySet()) {
			fillLangFile(entry);
		}

	}

	private static File findFile(String fileName) {
		Optional<File> possibleFile = Arrays.stream(langFiles)
				.filter(file -> file.getName().equals(fileName))
				.findAny();
		if (!possibleFile.isPresent()) {
			throw new RuntimeException(String.format("Could not find %s", fileName));
		}
		return possibleFile.get();
	}

	private static Map<String, String> buildMap(File langFile) {
		System.out.printf("Building key-value map for %s...\n", langFile.getName());
		Map<String, String> langMap = new HashMap<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(langFile));
			reader.lines().filter(s -> !s.startsWith("#"))
					.filter(s -> !s.isEmpty()).forEach(line -> {

				String[] tokens = line.split("=");
				langMap.put(tokens[0], tokens[1]);
			});
			reader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
		}
		System.out.printf("Read %d keys from file %s\n", langMap.keySet().size(), langFile.getName());
		return langMap;
	}

	private static Set<String> getKeys(File langFile) {
		System.out.printf("Getting keys for %s...\n", langFile.getName());
		Set<String> keys = new HashSet<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(langFile));
			reader.lines().filter(s -> !s.startsWith("#"))
					.filter(s -> !s.isEmpty()).forEach(line -> {

				String[] tokens = line.split("=");
				keys.add(tokens[0]);
			});
			reader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}
		System.out.printf("Read %d keys from file %s\n", keys.size(), langFile.getName());
		return keys;
	}

	private static void fillLangFile(Map.Entry<File, Set<String>> entry) {
		System.out.printf("Copying missing keys to %s\n", entry.getKey().getName());
		Set<String> missingKeys = Sets.difference(masterLangMap.keySet(), entry.getValue());
		if (missingKeys.isEmpty()) {
			System.out.println("No keys to add");
			return;
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(entry.getKey()));
			for (String key : missingKeys) {
				StringBuilder builder = new StringBuilder();
				builder.append(key);
				builder.append("=");
				builder.append(masterLangMap.get(key));
				builder.append("\n");
				writer.append(new String(builder));
			}
		} catch (Exception e) {
			System.out.printf("Error: %s\n", e.getMessage());
		} finally {
			IOUtils.closeQuietly(writer);
		}
		System.out.printf("%d new keys written to %s\n", missingKeys.size(), entry.getKey().getName());
	}
}
