package models;

public class Entry {
	/**
	 * エントリを定義
	 */
	private String user_sn = null; // ユーザーSN
	private String cat_id = null; // 費目ID
	private String item_id = null; // 品目ID
	private String cat_name = null; // 費目名
	private String item_name = null; // 品目名
	private String amount = null; // 金額
	private String time = null; // 時刻
	private String ratio = null; // 比率

	/**
	 * コンストラクタ
	 */
	public Entry() {
	}

	public Entry(String _user_sn, String _cat_id, String _item_id, String _amount, String _time) {
		user_sn = _user_sn;
		cat_id = _cat_id;
		item_id = _item_id;
		amount = _amount;
		time = _time;
	}

	/**
	 * エントリデータをセットする
	 */
	public void setUser(String user_sn) {
		this.user_sn = user_sn;
	};

	public void setCategoryID(String cat_id) {
		this.cat_id = cat_id;
	};

	public void setItemID(String item_id) {
		this.item_id = item_id;
	};

	public void setCategoryName(String cat_name) {
		this.cat_name = cat_name;
	};

	public void setItemName(String item_name) {
		this.item_name = item_name;
	};

	public void setAmount(String amount) {
		this.amount = amount;
	};

	public void setTime(String time) {
		this.time = time;
	};

	public void setRatio(String ratio) {
		this.ratio = ratio;
	};

	/**
	 * エントリデータを取得する
	 */
	public String getUser() {
		return user_sn;
	};

	public String getCategoryID() {
		return cat_id;
	};

	public String getItemID() {
		return item_id;
	};

	public String getCategoryName() {
		return cat_name;
	};

	public String getItemName() {
		return item_name;
	};

	public String getAmount() {
		return amount;
	};
	public String getTime() {
		return time;
	};
	public String getRatio() {
		return ratio;
	};
}