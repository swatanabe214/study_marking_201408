package jp.ktsystem.kadai201408.s_watanabe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	/** 文字コード */
	private static final String CHARACTER_CODE = "UTF-8";
	/** 無効な演算子（BOM） */
	private static final String BOM = "\uFEFF";

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
		long calcScoreSum = 0;

		if (null == anInputPath) {

			throw new KadaiException(ErrorCode.FILE_IN_OUT);

		}

		try {

			// ファイル読み込み
			File file = new File(anInputPath);

			// BufferedReaderを作る（文字コードを指定）
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARACTER_CODE));

			// 1行読み込み
			String str = br.readLine();

			// データがある時の処理
			if (null != str) {

				// BOM除去
				str = skipBOM(str);

				// 要素を配列に詰める
				String[] array = str.split(",", -1);

				// 配列の要素を取り出して計算する
				for (int i = 0; i < array.length; i++) {

					//
					// 文字を数字に置き換える
					//

					// 半角英数の小文字を大文字に変換
					String st = array[i].toUpperCase();

					// 数字・全角文字チェック
					validateHalfWidthEnglish(st);

					// 要素一つ一つに分解
					int sum = 0;
					for (int j = 0; j < st.length(); j++) {

						// 一文字
						char one = st.charAt(j);

						// 要素の中身（一文字ずつバラバラにしたもの）を足す
						long parts = ((int) one) - ((int) 'A' - 1);
						sum += parts;

					}

					// n + 1番目をかける
					long multiplyValue = sum * (i + 1);

					// 要素を一つずつ足す
					calcScoreSum += multiplyValue;

				}
			}

		} catch (IOException e) {

			throw new KadaiException(ErrorCode.FILE_IN_OUT);

		} finally {

			if (null != br) {

				try {

					br.close();

				} catch (IOException e) {

					// finallyでは例外投げない

				}

			}

		}
		return calcScoreSum;

	}

	/**
	 * <p>BOMをスキップします。</p>
	 *
	 * @param str ファイルの一行
	 * @return str BOM除去後のファイルの一行
	 */
	private static String skipBOM(String str) {

		if (str.startsWith(BOM)) {

			// BOMを取り外す
			str = str.substring(1);

		}

		return str;

	}

	/**
	 * <p>半角英字以外はエラーにして終了します。</p>
	 *
	 * @param String 半角英字の文字列
	 * @exception KadaiException 半角英字以外が存在する時の例外
	 */
	private static void validateHalfWidthEnglish(String aWord) throws KadaiException {

		if (!aWord.matches("[A-Z]*")) {

			// 半角英字以外はエラー
			throw new KadaiException(ErrorCode.INVALID_STRING);

		}

	}

}
