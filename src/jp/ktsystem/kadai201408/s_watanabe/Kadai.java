package jp.ktsystem.kadai201408.s_watanabe;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jp.ktsystem.kadai201408.common.ErrorCode;
import jp.ktsystem.kadai201408.common.KadaiException;

/**
 * <h3>データ出現位置を n とし、<br>
 * n とデータ点数の積の合計を求めるクラスです。</h3>
 *
 * @author s_watanabe
 *
 */
public class Kadai {

	/** 文字コード：UTF-8 */
	private static final String CHARACTER_CODE_UTF8 = "UTF-8";

	/**
	 * <p>ファイルを読み込み、<br>
	 * n とデータ点数の積の合計を求めます。</p>
	 *
	 * @param String 指定されたファイルパス
	 * @return calcScoreSum nとデータ点数の積の合計
	 * @exception KadaiException エラー発生時投げる例外
	 *
	 */
	public static long calcScoreSum(String anInputPath) throws KadaiException {

		BufferedReader br = null;
		FileInputStream fis = null;
		long calcScoreSum = 0;

		if (null == anInputPath) {

			throw new KadaiException(ErrorCode.FILE_IN_OUT);

		}

		try {

			// ファイル読み込み
			File file = new File(anInputPath);

			// BufferedReaderを作る（UTF-8を指定）
			br = new BufferedReader(new InputStreamReader(skipUTF8BOM(new FileInputStream(file), CHARACTER_CODE_UTF8)));

			// 1行読み込み
			String str = br.readLine();

			// データがある時の処理
			if (null != str) {

				// 要素を配列に詰める
				String[] array = str.split(",");

				// 配列の要素を取り出して計算する
				for (int n = 0; n < array.length; n++) {

					//
					// 文字を数字に置き換える
					//

					// 半角英数の小文字を大文字に変換
					String st = array[n].toUpperCase();

					// 数字・全角文字チェック
					validateHalfWidthEnglish(st);

					// 要素一つ一つに分解
					int sum = 0;
					for (int i = 0; i < st.length(); i++) {

						// 一文字
						char one = st.charAt(i);

						// 要素の中身（一文字ずつバラバラにしたもの）を足す
						long parts = ((int) one) - ((int) 'A' - 1);
						sum += parts;

					}

					// n + 1番目をかける
					long multiplyValue = sum * (n + 1);

					// 要素を一つずつ足す
					calcScoreSum += multiplyValue;

				}
			}

		} catch (FileNotFoundException e) {

			throw new KadaiException(ErrorCode.FILE_IN_OUT);

		} catch (IOException e) {

			throw new KadaiException(ErrorCode.FILE_IN_OUT);

		} finally {

			if (null != br && null != fis) {

				try {

					fis.close();
					br.close();

				} catch (IOException e) {

					// finallyでは例外投げない

				}

			}

		}
		return calcScoreSum;

	}

	/**
	 * <p>UTF-8のBOMをスキップします。</p>
	 *
	 * @param InputStream 読み込むファイル
	 * @param String 文字コード
	 * @return anInputStream ファイル
	 * @exception IOException
	 */
	private static InputStream skipUTF8BOM(InputStream anInputStream, String aCharSet) throws IOException {

		if (!CHARACTER_CODE_UTF8.equals(aCharSet.toUpperCase())) {

			return anInputStream;

		}

		if (!anInputStream.markSupported()) {

			// マーク機能が無い場合BufferedInputStreamを被せる
			anInputStream = new BufferedInputStream(anInputStream);

		}

		// 先頭にマークを付ける
		anInputStream.mark(3);

		if (3 <= anInputStream.available()) {

			byte b[] = new byte[3];
			anInputStream.read(b, 0, 3);

			if (b[0] != (byte) 0xEF || b[1] != (byte) 0xBB || b[2] != (byte) 0xBF) {

				// BOMでない場合は先頭まで巻き戻す
				anInputStream.reset();

			}
		}

		return anInputStream;
	}

	/**
	 * <p>半角英字以外はエラーにして終了します。</p>
	 *
	 * @param String 半角英字の文字列
	 * @exception KadaiException 半角英字以外が存在する時の例外
	 */
	private static void validateHalfWidthEnglish(String anArray) throws KadaiException {

		if (!anArray.matches("[A-Z]*")) {

			// 半角英字以外はエラー
			throw new KadaiException(ErrorCode.INVALID_STRING);

		}

	}





}
