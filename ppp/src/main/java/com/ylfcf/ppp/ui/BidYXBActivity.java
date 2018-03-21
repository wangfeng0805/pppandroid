package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncYXBInvest;
import com.ylfcf.ppp.async.AsyncYXBProductLog;
import com.ylfcf.ppp.async.AsyncYXBUserAccount;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.YXBProductLogInfo;
import com.ylfcf.ppp.entity.YXBUserAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Ԫ�ű�---��ҪͶ��
 * 
 * @author Administrator
 * 
 */
public class BidYXBActivity extends BaseActivity implements OnClickListener {
	private static final String className = "BidYXBActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;

	private TextView userBalanceTV;// �û��������
	private TextView borrowBalanceTV;// ʣ��ļ�����
	private Button rechargeBtn;// ��ֵ

	private Button descendBtn;// �ݼ���ť
	private Button increaseBtn;// ������ť
	private Button investBtn;// Ͷ��
	private EditText investMoneyET;
	private ImageView deleteImg;// x��
	private TextView yjsyText;// Ԥ������
	private TextView agreementText;// ����Э��
	private CheckBox cb;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private double yxbInvestRate = 0.06;// Ԫ�ű����껯����
	private YXBUserAccountInfo yxbUserAccountInfo;
	private UserRMBAccountInfo userRMBAccountInfo;
	private YXBProductLogInfo yxbProductLogInfo;

	private float sunInvestMoney = 0;// �û��ܹ�Ͷ�Ľ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bid_yxb_activity);
		findViews();
		requestYXBProductLog("", sdf.format(new Date()));
		requestUserAccountInfo(SettingsManager
				.getUserId(getApplicationContext()));
		requestYxbUserAccount(SettingsManager
				.getUserId(getApplicationContext()));
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("�Ϲ�");

		borrowName = (TextView) findViewById(R.id.bid_yxb_activity_borrow_name);
		borrowName.setText("Ԫ�ű������ڣ�");
		userBalanceTV = (TextView) findViewById(R.id.bid_yxb_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_yxb_activity_borrow_balance);
		agreementText = (TextView) findViewById(R.id.bid_yxb_activity_agreement);
		agreementText.setOnClickListener(this);
		rechargeBtn = (Button) findViewById(R.id.bid_yxb_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_yxb_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		increaseBtn = (Button) findViewById(R.id.bid_yxb_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		investMoneyET = (EditText) findViewById(R.id.bid_yxb_activity_invest_et);
		deleteImg = (ImageView) findViewById(R.id.bid_yxb_activity_delete);
		deleteImg.setOnClickListener(this);
		investBtn = (Button) findViewById(R.id.bid_yxb_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_yxb_activity_yjsy);
		cb = (CheckBox) findViewById(R.id.bid_yxb_activity_cb);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				String moneyA = investMoneyET.getText().toString();
				if (isChecked) {
					investBtn.setEnabled(true);
				} else {
					investBtn.setEnabled(false);
				}
			}
		});
		investMoneyET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String moneyA = investMoneyET.getText().toString();
				if (cb.isChecked() && !moneyA.isEmpty()) {
					investBtn.setEnabled(true);
				} else {
					investBtn.setEnabled(false);
				}
				double moneyD = 0;
				try {
					moneyD = Double.parseDouble(moneyA);
					if (moneyD > 500000) {
						investMoneyET.setText("500000");
						Util.toastLong(BidYXBActivity.this, "ÿ������Ϲ����Ϊ50W�����");
					} else {
						int investMoney = 0;
						investMoney = Integer.parseInt(moneyA);
						computeIncome(yxbInvestRate, investMoney);
					}
				} catch (Exception e) {
					yjsyText.setText("0.00");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_yxb_activity_recharge_btn:
			Intent intent = new Intent(BidYXBActivity.this,
					RechargeActivity.class);
			startActivity(intent);
			break;
		case R.id.bid_yxb_activity_discend_btn:
			// �ݼ�
			investMoneyDescend();
			break;
		case R.id.bid_yxb_activity_increase_btn:
			// ����
			investMoneyIncrease();
			break;
		case R.id.bid_yxb_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_yxb_activity_borrow_bidBtn:
			// Ԫ�ű��Ϲ�
			checkYXBInvestData();
			break;
		case R.id.bid_yxb_activity_agreement:
			// ����Э��
			Intent intentAgreement = new Intent(BidYXBActivity.this,
					YXBAgreementActivity.class);
			startActivity(intentAgreement);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsResume(this);//����ͳ��ʱ��
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//����ͳ��ҳ����ת
		UMengStatistics.statisticsPause(this);//����ͳ��ʱ��
	}

	/**
	 * ����Ϲ�����Ƿ����Ҫ��
	 */
	private void checkYXBInvestData() {
		int investMoneyInt = 0;
		String investMoneyStr = investMoneyET.getEditableText().toString();
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		String sumInvestMoneyStr = null;// �û����Ϲ����
		float userAccountBalance = 0;// �û��˻����
		float needRaiseMoney = 0;// ʣ��ļ�����
		try {
			sumInvestMoneyStr = yxbUserAccountInfo.getSum_invest_money();
			sunInvestMoney = Float.parseFloat(sumInvestMoneyStr);
		} catch (Exception e) {
		}
		try {
			userAccountBalance = Float.parseFloat(userRMBAccountInfo
					.getUse_money());
		} catch (Exception e) {
		}
		try {
			needRaiseMoney = Float.parseFloat(yxbProductLogInfo
					.getNeed_raise_money());
		} catch (Exception e) {
		}
		if (investMoneyStr.isEmpty()) {
			Util.toastLong(BidYXBActivity.this, "�������Ϲ����");
		} else if (investMoneyInt > userAccountBalance) {
			Util.toastLong(BidYXBActivity.this, "�˻����㣬���ֵ");
		} else if (investMoneyInt > (500000 - sunInvestMoney)) {
			Util.toastLong(BidYXBActivity.this, "�Ϲ��������ǰ��ʣ���Ϲ����Ϊ��"
					+ (500000 - sunInvestMoney) + "Ԫ");
		} else if (investMoneyInt > needRaiseMoney) {
			Util.toastLong(BidYXBActivity.this, "ʣ��ļ����Ȳ���");
		} else if (investMoneyInt < 100) {
			Util.toastLong(BidYXBActivity.this, "�Ϲ���������100Ԫ");
		} else if (investMoneyInt % 100 != 0) {
			Util.toastLong(BidYXBActivity.this, "�Ϲ�������Ϊ100��������");
		} else {
			showInvestDialog(investMoneyInt);
		}
	}

	/**
	 * �ݼ�
	 */
	private void investMoneyDescend() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		if (investMoneyInt <= 100) {
			investMoneyInt = 0;
		} else {
			investMoneyInt -= 100;
		}
		if (investMoneyInt <= 0) {
			investMoneyET.setText(null);
		} else {
			investMoneyET.setText(investMoneyInt + "");
		}
	}

	/**
	 * ����
	 */
	private void investMoneyIncrease() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		investMoneyInt += 100;
		investMoneyET.setText(investMoneyInt + "");
	}

	private void resetInvestMoneyET() {
		if (investMoneyET != null) {
			investMoneyET.setText(null);
			yjsyText.setText("0.00");
		}
	}

	/**
	 * ��������
	 * 
	 * @param rate
	 * @param investMoney
	 * 
	 */
	private void computeIncome(double rate, int investMoney) {
		double incomeDouble = rate / 365 * investMoney;
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		df.setRoundingMode(RoundingMode.FLOOR);// ����������
		if (incomeDouble < 1) {
			yjsyText.setText("0" + df.format(incomeDouble));
		} else {
			yjsyText.setText(df.format(incomeDouble));
		}
	}

	/**
	 * ���ʣ���Ͷ���
	 * 
	 * @param loginfo
	 */
	private void initBorrowBalance(YXBProductLogInfo loginfo) {
		String needRaiseMoney = loginfo.getNeed_raise_money();
		String raiseMoney = loginfo.getRaise_money();
		double raiseBalance = 0d;
		try {
			double needRaiseMoneyD = Double.parseDouble(needRaiseMoney);
			double raiseMoneyD = Double.parseDouble(raiseMoney);
			raiseBalance = needRaiseMoneyD - raiseMoneyD;
		} catch (Exception e) {
		}
		borrowBalanceTV.setText(Util.commaSpliteData(String
				.valueOf(raiseBalance)));
	}

	/**
	 * ȷ���Ϲ���dialog
	 */
	private void showInvestDialog(final int investMoneyInt) {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.invest_prompt_layout, null);
		LinearLayout detailLayout = (LinearLayout) contentView
				.findViewById(R.id.invest_prompt_yjb_layout_detail);
		detailLayout.setVisibility(View.GONE);
		Button sureBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_surebtn);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_cancelbtn);
		TextView totalMoney = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		TextView text1 = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_text1);
		text1.setText("��ȷ���Ϲ�");
		totalMoney.setText(String.valueOf(investMoneyInt));
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (yxbProductLogInfo != null) {
					requestYxbInvest(yxbProductLogInfo.getProduct_id(),
							SettingsManager.getUserId(getApplicationContext()),
							String.valueOf(investMoneyInt));
				} else {
					requestYxbInvest("1",
							SettingsManager.getUserId(getApplicationContext()),
							String.valueOf(investMoneyInt));
				}
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// ��������������ˣ���������ʾ����
		dialog.show();
		// ����dialog�Ŀ��
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * Ԫ�ű�ÿ��ͳ��
	 * 
	 * @param id
	 * @param dateTime
	 */
	private void requestYXBProductLog(String id, String dateTime) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncYXBProductLog productLogTask = new AsyncYXBProductLog(
				BidYXBActivity.this, id, dateTime, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								yxbProductLogInfo = baseInfo
										.getYxbProductLogInfo();
								initBorrowBalance(yxbProductLogInfo);
							}
						}
					}
				});
		productLogTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �û��˻���Ϣ
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidYXBActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								userRMBAccountInfo = baseInfo
										.getRmbAccountInfo();
								userBalanceTV.setText(userRMBAccountInfo
										.getUse_money());
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * Ԫ�ű�Ͷ�ʽӿ�
	 * 
	 * @param productId
	 * @param userId
	 * @param orderMoney
	 */
	private void requestYxbInvest(String productId, String userId,
			String orderMoney) {
		AsyncYXBInvest yxbInvestTask = new AsyncYXBInvest(BidYXBActivity.this,
				productId, userId, orderMoney, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Intent intent = new Intent(BidYXBActivity.this,
										YXBBidSuccessActivity.class);
								startActivity(intent);
								finish();
							} else {
								Util.toastLong(BidYXBActivity.this,
										baseInfo.getMsg());
							}
						}
					}
				});
		yxbInvestTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * Ԫ�ű����˻���Ϣ
	 * 
	 * @param userId
	 */
	private void requestYxbUserAccount(String userId) {
		AsyncYXBUserAccount yxbAccountTask = new AsyncYXBUserAccount(
				BidYXBActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								yxbUserAccountInfo = baseInfo
										.getYxbUserAccountInfo();
							}
						}
					}
				});
		yxbAccountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
