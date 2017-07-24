package com.healthyfish.healthyfish.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {
	public static String read(String addr) {
		File file = new File(addr);

		StringBuilder sb = new StringBuilder();
		String s = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}

			br.close();
			String str = sb.toString();
			return str;
		} catch (Exception e) {
			return addr + "���������ļ�";
		}

	}

	public static String write(String addr, String text) {
		try {

			FileWriter fw = new FileWriter(addr, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(text);
			pw.close();
			fw.close();

			return "�ɹ�";
		} catch (IOException e) {
			e.printStackTrace();
			return "ʧ��";
		}
	}
}
