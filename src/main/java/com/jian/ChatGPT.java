package com.jian;

/**
 * @author Jian Qi
 * @Date 2022/12/23 23:49
 * @Description chatGPT AI
 * @Version 0.1
 */

import org.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
//import org.json.JSONObject;

public class ChatGPT {
	private static final String API_KEY = "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";
	private static final String ENDPOINT = "https://api.openai.com/v1/completions";

	public static void a() {
		String a = "\n\n$ ls\n\nbin boot dev etc home lib lib64 media mnt opt proc root run sbin srv sys tmp usr var";

		int index = a.indexOf("\n\n");
		String c = null;
		if (index == -1) {
			c = a;
		} else {
			c = a.substring(index + 2);
		}
		System.out.println(index + " - c: " + c);
		System.out.println("a : " + a.trim());


		String b[] = a.split("\n\n");

//		int i = 1;
		System.out.println(b.length);
		System.out.println("第0行: " + b[0]);
		a = "";
		for (int i = 1; i < b.length; i++) {
			a += b[i];
			System.out.println("第" + i + "行: " + b[i]);
		}
		System.out.println("结果为: " + a);


	}

	public static void chat() throws IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(ENDPOINT);
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Authorization", "Bearer " + API_KEY);

		String model = "text-davinci-003";
		/*
		我要你假装是一个Linux终端。我会输入命令，你回复终端应该显示什么。我希望你只用一个唯一的代码块里的终端输出回复，别的都不要。不要输出解释。除非我指示你这样做，否则不要输入命令。接下来全部是这个要求。第二条指令:ls
		 */
		String prompt = "回答我上一句问了什么问题";
		int maxTokens = 1024;

		JSONObject payload = new JSONObject();
		payload.put("model", model);
		payload.put("prompt", prompt);
		payload.put("max_tokens", maxTokens);

		System.out.println("提问: " + prompt);
		request.setEntity(new StringEntity(payload.toString(), "UTF-8"));

		HttpResponse response = httpClient.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			InputStream inputStream = response.getEntity().getContent();
			StreamToString streamToString = new StreamToString();
			streamToString.streamToString(inputStream);

		} else {
			// handle error
		}
	}

	public static void main(String[] args) throws Exception {
//		a();
//		chat();

		String s = "@しずか 说话";
		boolean is = s.contains("@" + "しずか");
		System.out.println(is);
	}


}

class StreamToString {
	public String streamToString(InputStream inputStream) throws IOException {
		// handle response
		// get response content
//		InputStream inputStream = response.getEntity().getContent();
		String responseContent = IOUtils.toString(inputStream, "UTF-8");

		// parse response content as JSON
		JSONObject jsonResponse = new JSONObject(responseContent);

		// get the generated text
		String generatedText = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text");

		System.out.println("\n接收到的为:");
		System.out.println(jsonResponse.toString());

		System.out.println("\n回答为:");
		// print the generated text
		String result = generatedText;//.split("\\n\\n")[1];
		System.out.println(result);
		return "";
	}
}

//快速排序
class QuickSort {
	/**
	 * 快速排序  时间复杂度  平均O(nlogn)  最坏O(n2)
	 *
	 * @param data
	 * @param start
	 * @param end
	 */
	public static void quickSort(int[] data, int start, int end) {
		if (start >= end) return;

		// 设置枢轴
		int pivot = data[start];

		// 标记起始值
		int marker = start;

		// 从start开始，找到第一个大于枢轴的位置
		for (int i = start + 1; i <= end; i++) {
			// 如果当前元素大于pivot
			if (data[i] > pivot) {
				// 递增  marker
				marker++;
				// 将当前元素和 marker处的元素交换  即属于前半部分的元素进行交换
				int temp = data[marker];
				data[marker] = data[i];
				data[i] = temp;
			}
		}
		// 交换start 和 marker处的元素
		int temp = data[start];
		data[start] = data[marker];
		data[marker] = temp;
		// 使用分治法  进行排序
		quickSort(data, start, marker - 1);
		quickSort(data, marker + 1, end);
	}
}