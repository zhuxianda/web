package com.zxd.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AccountDao {

	@Autowired
	@Qualifier(value = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public boolean isUidExist(Long uid) {
		String sql = "select * from account where uid = ?";
		return this.jdbcTemplate.queryForList(sql, uid).size() > 0;
	}

//	public List<Account> getAllAccount() {
//		String sql = "select uid,balance from account where state=0";
//		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
//			@Override
//			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Account account = new Account();
//				account.setUid(rs.getLong("uid"));
//				account.setBalance(rs.getBigDecimal("balance"));
//				return account;
//			}
//		});
//		return list;
//	}
//
//	public List<Account> getAccountFilterUsergroup(String usergroup) {
//		String sql = "select uid,balance,virtualCoin from account where state=0";
//		if (usergroup.equals("inner"))
//			sql += " and uid in (select uid from accountForOperator)";
//		else if (usergroup.equals("outer"))
//			sql += " and uid not in (select uid from accountForOperator)";
//		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
//			@Override
//			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Account account = new Account();
//				account.setUid(rs.getLong("uid"));
//				account.setBalance(rs.getBigDecimal("balance"));
//				account.setVirtualCoin(rs.getBigDecimal("virtualCoin"));
//				return account;
//			}
//		});
//		return list;
//	}
//
//	public List<Map<String, Object>> getAccountAuthorization(String userType) {
//		String sql = "select uid,isAuthorized from account where state=0";
//		if (userType.equals("inner"))
//			sql += " and uid in (select uid from accountForOperator)";
//		else if (userType.equals("outer"))
//			sql += " and uid not in (select uid from accountForOperator)";
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
//		return list;
//	}
//
//	public Map<String, Object> getAccountAuthorization(Long uid) {
//		String sql = "select uid,isAuthorized from account where state=0 and uid = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		return list.size() > 0 ? list.get(0) : new HashMap<String, Object>();
//	}
//
//	public Map<String, Object> getAccountAuthorization(Set<Long> uids) {
//		String sql = "select uid,isAuthorized from account where state=0 and uid in (" + StringHelper.joinSet(uids, ",") + ")";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
//		return list.size() > 0 ? list.get(0) : new HashMap<String, Object>();
//	}
//
//	public int updateAuthorization(Long uid) {
//		String sql = "update account set isAuthorized = 1 where uid = ?";
//		return this.jdbcTemplate.update(sql, uid);
//	}
//
//	public List<Account> getAllAccountForMessage() {
//		String sql = "select uid, hasSendAccountBalanceMessage,balance,virtualCoin from account where state=0 and uid not in (select uid from accountForOperator)";
//		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
//			@Override
//			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Account account = new Account();
//				account.setUid(rs.getLong("uid"));
//				account.setBalance(rs.getBigDecimal("balance"));
//				account.setVirtualCoin(rs.getBigDecimal("virtualCoin"));
//				account.setHasSendAccountBalanceMessage(rs.getInt("hasSendAccountBalanceMessage"));
//				return account;
//			}
//		});
//		return list;
//	}
//
//	public List<Account> getAllDevelopAccount() {
//		String sql = "select * from account where uid not in (select uid from accountForOperator)";
//		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
//			@Override
//			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Account account = new Account();
//				account.setUid(rs.getLong("uid"));
//				account.setName(rs.getString("name"));
//				account.setBalance(rs.getBigDecimal("balance"));
//				account.setIsAcceptProtocol(rs.getInt("isAcceptProtocol"));
//				account.setRemark(rs.getString("remark"));
//				account.setCreateTimestamp(rs.getLong("createTime"));
//				account.setLastModifyTime(rs.getString("lastModifyTime"));
//				return account;
//			}
//		});
//		return list;
//	}
//
//	public List<Account> getAllDevelopAccount(String user) {
//		String sql = "select * from account where uid not in (select uid from accountForOperator) ";
//		if (StringUtils.hasText(user)) {
//			if (StringUtil.isNumeric(user))
//				sql += " and uid = " + user;
//			else
//				sql += " and name like '%" + user + "%'";
//		}
//
//		List<Account> list = jdbcTemplate.query(sql, new RowMapper<Account>() {
//			@Override
//			public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
//				Account account = new Account();
//				account.setUid(rs.getLong("uid"));
//				account.setName(rs.getString("name"));
//				account.setBalance(rs.getBigDecimal("balance"));
//				account.setIsAcceptProtocol(rs.getInt("isAcceptProtocol"));
//				account.setRemark(rs.getString("remark"));
//				account.setCreateTimestamp(rs.getLong("createTime"));
//				account.setLastModifyTime(rs.getString("lastModifyTime"));
//				return account;
//			}
//		});
//		return list;
//	}
//
//	public Page<Account> getAllDevelopAccount(String user, Integer pageSize, Integer pageNo, String sort) {
//		String sql = "select * from account where uid not in (select uid from accountForOperator) ";
//		if (StringUtils.hasText(user)) {
//			if (StringUtil.isNumeric(user))
//				sql += " and uid = " + user;
//			else
//				sql += " and name like '%" + user + "%'";
//		}
//		if (StringUtils.hasText(sort)) {
//			sql += " order by " + sort + " desc";
//		}
//
//		String countSql = "select count(1) from (" + sql + ") as a";
//		sql += " limit " + (pageNo - 1) * pageSize + "," + pageSize;
//
//		Page<Account> page = new Page<Account>(pageNo, pageSize, jdbcTemplate.queryForLong(countSql.toString()),
//				jdbcTemplate.query(sql, new RowMapper<Account>() {
//					@Override
//					public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
//						Account account = new Account();
//						account.setUid(rs.getLong("uid"));
//						account.setName(rs.getString("name"));
//						account.setBalance(rs.getBigDecimal("balance"));
//						account.setIsAcceptProtocol(rs.getInt("isAcceptProtocol"));
//						account.setRemark(rs.getString("remark"));
//						account.setCreateTimestamp(rs.getLong("createTime"));
//						account.setLastModifyTime(rs.getString("lastModifyTime"));
//						return account;
//					}
//				}));
//		return page;
//	}
//
//	public List<DetailAccount> getAllDevelopDetailAccount(String user) {
//		String sql = "select t1.uid, t1.name, t1.createTime, t1.lastModifyTime, t1.remark, t1.isAcceptProtocol, "
//				+ " t2.type, t2.companyName, t2.contact, t2.bankAccount, t2.bankAccountName, t2.province, t2.city, t2.bank, t2.subbranch, t2.qq, t2.email, t2.tel "
//				+ " from account t1 inner join detailAccount t2 on t1.uid = t2.uid  where t2.status = 1";
//
//		if (StringUtils.hasText(user)) {
//			if (StringUtil.isNumeric(user))
//				sql += " and uid = " + user;
//			else
//				sql += " and name like '%" + user + "%'";
//		}
//
//		List<DetailAccount> list = jdbcTemplate.query(sql, new RowMapper<DetailAccount>() {
//			@Override
//			public DetailAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
//				DetailAccount account = new DetailAccount();
//				account.setUid(rs.getLong("uid"));
//				account.setName(rs.getString("name"));
//				account.setRemark(rs.getString("remark"));
//				account.setIsAcceptProtocol(rs.getInt("isAcceptProtocol"));
//				account.setCreateTime(rs.getLong("createTime"));
//				account.setLastModifyTime(rs.getString("lastModifyTime"));
//				account.setType(rs.getString("type"));
//				account.setCompanyName(rs.getString("companyName"));
//				account.setContact(rs.getString("contact"));
//				account.setBankAccount(rs.getString("bankAccount"));
//				account.setBankAccountName(rs.getString("bankAccountName"));
//				account.setProvince(rs.getString("province"));
//				account.setCity(rs.getString("city"));
//				account.setBank(rs.getString("bank"));
//				account.setSubbranch(rs.getString("subbranch"));
//				account.setQq(rs.getString("qq"));
//				account.setEmail(rs.getString("email"));
//				account.setTel(rs.getString("tel"));
//				return account;
//			}
//		});
//		return list;
//	}
//
//	public List<DetailAccount> getAllDevelopDetailAccount(String user, Long start, Long end) {
//		String sql = "select t1.uid, t1.name, t1.createTime, t1.lastModifyTime, t1.remark, t1.isAcceptProtocol, t1.isArriveEStation, "
//				+ " t2.type, t2.companyName, t2.contact, t2.bankAccount, t2.bankAccountName, t2.province, t2.city, t2.bank, t2.subbranch, t2.qq, t2.email, t2.tel "
//				+ "from account t1 left join detailAccount t2 "
//				+ "on t1.uid = t2.uid where (t2.status is NULL or t2.status <> 0) ";
//		if (StringUtils.hasText(user)) {
//			if (StringUtil.isNumeric(user))
//				sql += " and ( t1.uid = " + user + " or t1.name like '%" + user + "%' )";
//			else
//				sql += " and t1.name like '%" + user + "%'";
//		} else {
//			sql += " and t1.createTime >= " + start + " and t1.createTime <= " + end;
//		}
//
//		List<DetailAccount> list = jdbcTemplate.query(sql, new RowMapper<DetailAccount>() {
//			@Override
//			public DetailAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
//				DetailAccount account = new DetailAccount();
//				account.setUid(rs.getLong("uid"));
//				account.setName(rs.getString("name"));
//				account.setRemark(rs.getString("remark"));
//				account.setIsAcceptProtocol(rs.getInt("isAcceptProtocol"));
//				account.setIsArriveEStation(rs.getInt("isArriveEStation"));
//				account.setCreateTime(rs.getLong("createTime"));
//				account.setLastModifyTime(rs.getString("lastModifyTime"));
//				account.setType(rs.getString("type"));
//				account.setCompanyName(rs.getString("companyName"));
//				account.setContact(rs.getString("contact"));
//				account.setBankAccount(rs.getString("bankAccount"));
//				account.setBankAccountName(rs.getString("bankAccountName"));
//				account.setProvince(rs.getString("province"));
//				account.setCity(rs.getString("city"));
//				account.setBank(rs.getString("bank"));
//				account.setSubbranch(rs.getString("subbranch"));
//				account.setQq(rs.getString("qq"));
//				account.setEmail(rs.getString("email"));
//				account.setTel(rs.getString("tel"));
//				return account;
//			}
//		});
//		return list;
//	}
//
//	public List<DetailAccount> getAllDevelopDetailAccount(Long start, Long end) {
//		String sql = "select t1.uid, t1.name, t1.createTime, t1.lastModifyTime, t1.remark, t1.isAcceptProtocol, t1.isArriveEStation, "
//				+ " t2.type, t2.companyName, t2.contact, t2.bankAccount, t2.bankAccountName, t2.province, t2.city, t2.bank, t2.subbranch, t2.qq, t2.email, t2.tel "
//				+ "from account t1 left join detailAccount t2 "
//				+ "on t1.uid = t2.uid where (t2.status is NULL or t2.status <> 0) "
//				+ " and t1.createTime >= ? and t1.createTime <= ?";
//
//		List<DetailAccount> list = jdbcTemplate.query(sql, new RowMapper<DetailAccount>() {
//			@Override
//			public DetailAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
//				DetailAccount account = new DetailAccount();
//				account.setUid(rs.getLong("uid"));
//				account.setName(rs.getString("name"));
//				account.setRemark(rs.getString("remark"));
//				account.setIsAcceptProtocol(rs.getInt("isAcceptProtocol"));
//				account.setIsArriveEStation(rs.getInt("isArriveEStation"));
//				account.setCreateTime(rs.getLong("createTime"));
//				account.setLastModifyTime(rs.getString("lastModifyTime"));
//				account.setType(rs.getString("type"));
//				account.setCompanyName(rs.getString("companyName"));
//				account.setContact(rs.getString("contact"));
//				account.setBankAccount(rs.getString("bankAccount"));
//				account.setBankAccountName(rs.getString("bankAccountName"));
//				account.setProvince(rs.getString("province"));
//				account.setCity(rs.getString("city"));
//				account.setBank(rs.getString("bank"));
//				account.setSubbranch(rs.getString("subbranch"));
//				account.setQq(rs.getString("qq"));
//				account.setEmail(rs.getString("email"));
//				account.setTel(rs.getString("tel"));
//				return account;
//			}
//		}, start, end);
//		return list;
//	}
//
//	public Account getAccount(long uid) {
//		String sql = "select * from account where uid = ? and isAcceptProtocol = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid, 1);
//		if (list.size() == 1) {
//			Map<String, Object> map = list.get(0);
//			Account account = new Account();
//			account.setId((Integer) map.get("id"));
//			account.setUid(Long.parseLong(map.get("uid").toString()));
//			account.setName((String) map.get("name"));
//			account.setBalance(new BigDecimal(map.get("balance").toString()));
//			account.setState((Integer) map.get("state"));
//			account.setVirtualCoin(new BigDecimal(map.get("virtualCoin").toString()));
//			return account;
//		} else {
//			return null;
//		}
//	}
//
//	public Integer getIsAcceptProtocol(Long uid) {
//		String sql = "select isAcceptProtocol from account where uid = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		return list.size() > 0 ? (Integer) list.get(0).get("isAcceptProtocol") : -1;
//	}
//
//	public Map<String, Object> getAccountBudMap(Long uid) {
//		String sql = "select id,accountBud,accountBudType from account where uid = ?";
//		DecimalFormat decimalFormat = new DecimalFormat("0.00");
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		Map<String, Object> res = new HashMap<String, Object>();
//		if (list.size() > 0) {
//			res.put("accountBud", decimalFormat.format((Double) list.get(0).get("accountBud")));
//			res.put("accountBudType", (Integer) list.get(0).get("accountBudType"));
//			res.put("accountId", (Integer) list.get(0).get("id"));
//
//		}
//		return res;
//	}
//
//	public Integer getIsConfirmTips(Long uid) {
//		String sql = "select isConfirmTips from account where uid = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		return list.size() > 0 ? (Integer) list.get(0).get("isConfirmTips") : -1;
//	}
//
//	public Account getAccountIgnoreProtocol(Long uid) {
//		String sql = "select * from account where uid = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		if (list.size() == 1) {
//			Map<String, Object> map = list.get(0);
//			Account account = new Account();
//			account.setId((Integer) map.get("id"));
//			account.setUid(Long.parseLong(map.get("uid").toString()));
//			account.setName((String) map.get("name"));
//			account.setBalance(new BigDecimal(map.get("balance").toString()));
//			account.setState((Integer) map.get("state"));
//			account.setIsArriveEStation((Integer) map.get("isArriveEStation"));
//			account.setVirtualCoin(new BigDecimal(map.get("virtualCoin").toString()));
//			return account;
//		} else {
//			return null;
//		}
//	}
//
//	public Map<String, AccountInfo> getAccountsInfo(String username) {
//		String sql = "select a.uid as uid, a.name as username, o.notes as notes ,a.balance as balance, a.virtualCoin as virtualCoin "
//				+ "from account a left join accountForOperator o on a.uid = o.uid";
//		if (StringUtils.hasText(username)) {
//			sql += " where a.name='" + username + "'";
//		}
//		return setAccountInfoMap(sql);
//	}
//
//	public Map<String, AccountInfo> getFuzzyAccountsInfo(String userInfo) {
//		String sql = "select a.uid as uid, a.name as username, o.notes as notes ,a.balance as balance, a.virtualCoin as virtualCoin "
//				+ "from account a left join accountForOperator o on a.uid = o.uid";
//		if (StringUtils.hasText(userInfo)) {
//			Long uid = -1L;
//			try {
//				uid = Long.parseLong(userInfo);
//				sql += " where (a.name like '%" + userInfo + "%' or a.uid = " + uid + ")";
//			} catch (Exception e) {
//				sql += " where a.name like '%" + userInfo + "%'";
//			}
//		}
//		return setAccountInfoMap(sql);
//	}
//
//	public Map<String, AccountInfo> getAccountsInfo(String userInfo, String userType) {
//		String sql = "select a.uid as uid, a.name as username, o.notes as notes ,a.balance as balance, a.virtualCoin as virtualCoin "
//				+ "from account a left join accountForOperator o on a.uid = o.uid";
//		if (StringUtils.hasText(userInfo)) {
//			Long uid = null;
//			try {
//				uid = Long.parseLong(userInfo);
//				sql += " where ( a.uid = " + uid + " or a.name = '" + uid + "')";
//			} catch (Exception e) {
//				sql += " where a.name = '" + userInfo + "'";
//			}
//		}
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
//		Map<String, AccountInfo> accounts = new HashMap<String, AccountInfo>();
//		for (Map<String, Object> map : list) {
//			AccountInfo accountInfo = new AccountInfo();
//			if (userType.equals("inner")) { // 外部帐号没有notes
//				if (map.get("notes") == null)
//					continue;
//			}
//			if (userType.equals("outer")) {
//				if (map.get("notes") != null)
//					continue;
//			}
//			String username = (String) map.get("username");
//			Double balance = (Double) map.get("balance");
//			Double virtualCoin = (Double) map.get("virtualCoin");
//			String notes = "-";
//			if (map.get("notes") != null && StringUtils.hasText(notes)) {
//				notes = (String) map.get("notes");
//			}
//			accountInfo.setUsername(username);
//			accountInfo.setBalance(balance);
//			accountInfo.setVirtualCoin(virtualCoin);
//			accountInfo.setNotes(notes);
//			accounts.put(map.get("uid").toString(), accountInfo);
//		}
//		return accounts;
//	}
//
//	private Map<String, AccountInfo> setAccountInfoMap(String sql) {
//		Map<String, AccountInfo> result = new HashMap<String, AccountInfo>();
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
//		for (Map<String, Object> map : list) {
//			AccountInfo accountInfo = new AccountInfo();
//			String username = (String) map.get("username");
//			Double balance = (Double) map.get("balance");
//			Double virtualCoin = (Double) map.get("virtualCoin");
//			String notes = "-";
//			if (map.get("notes") != null && StringUtils.hasText(notes)) {
//				notes = (String) map.get("notes");
//			}
//			accountInfo.setUsername(username);
//			accountInfo.setBalance(balance);
//			accountInfo.setVirtualCoin(virtualCoin);
//			accountInfo.setNotes(notes);
//			result.put(map.get("uid").toString(), accountInfo);
//		}
//		return result;
//	}
//
//	public Map<String, Map<String, Object>> getAccounts(String username) {
//		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
//		String sql = "select a.uid as uid, a.name as username, o.notes as notes ,a.balance, a.virtualCoin from account a left join accountForOperator o on a.uid = o.uid";
//		if (StringUtils.hasText(username)) {
//			sql += " where a.name='" + username + "'";
//		}
//		return setAccountMap(result, sql);
//	}
//
//	private Map<String, Map<String, Object>> setAccountMap(Map<String, Map<String, Object>> result, String sql) {
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
//		for (Map<String, Object> map : list) {
//			String notes = (String) map.get("notes");
//			if (!StringUtils.hasText(notes)) {
//				map.put("notes", "-");
//			}
//			result.put(map.get("uid").toString(), map);
//		}
//		return result;
//	}
//
//	// public Map<String, Map<String, Object>> getAccounts() {
//	// Map<String, Map<String, Object>> result = new HashMap<String, Map<String,
//	// Object>>();
//	// String sql =
//	// "select a.uid as uid, a.name as username, o.notes as notes ,a.balance, a.virtualCoin from account a left join accountForOperator o on a.uid = o.uid";
//	//
//	// return setAccountMap(result, sql);
//	// }
//
//	public void createAccount(Account account) {
//		String sql = "insert into account (uid, name, state, balance, virtualCoin,createTime,isAcceptProtocol, isArriveEStation) values (?,?,?,?,?,?,?,?)";
//		this.jdbcTemplate.update(sql, account.getUid(), account.getName(), account.getState(), account.getBalance(),
//				account.getVirtualCoin(), System.currentTimeMillis(), account.getIsAcceptProtocol(),
//				account.getIsArriveEStation());
//	}
//
//	public void acceptingProtocol(long uid) {
//		String sql = "update account set isAcceptProtocol = ? where uid = ?";
//		this.jdbcTemplate.update(sql, Constant.ACCOUNT_ACCEPT_PROTOCOL_TRUE, uid);
//	}
//
//	public void updateIsAcceptProtocol(int isAcceptProtocol, Long uid) {
//		String sql = "update account set isAcceptProtocol = ? where uid = ?";
//		this.jdbcTemplate.update(sql, isAcceptProtocol, uid);
//	}
//
//	public void confirmTips(long uid) {
//		String sql = "update account set isConfirmTips = ? where uid = ?";
//		this.jdbcTemplate.update(sql, Constant.ACCOUNT_CONFIRM_TIPS_TRUE, uid);
//	}
//
//	public void updateHasSendAccountBalanceMessage(Long uid, Integer type) {
//		String sql = "update account set hasSendAccountBalanceMessage = ? where uid = ?";
//		this.jdbcTemplate.update(sql, type, uid);
//	}
//
//	public void updateAccountBud(Long uid, String accountBud, Integer accountBudType) {
//		String sql = "update account set accountBud = ?, accountBudType = ? where uid = ?";
//		this.jdbcTemplate.update(sql, accountBud, accountBudType, uid);
//
//	}
//
//	public void updateIsArriveEStation(int isArriveEStation, Long uid) {
//		String sql = "update account set isArriveEStation = ? where uid = ?";
//		this.jdbcTemplate.update(sql, isArriveEStation, uid);
//	}
//
//	public void disableAccount(long uid) {
//		String sql = "update account set state = ? where uid = ?";
//		this.jdbcTemplate.update(sql, Constant.ACCOUNT_STATE_DISABLE, uid);
//	}
//
//	public void enableAccount(long uid) {
//		String sql = "update account set state = ? where uid = ?";
//		this.jdbcTemplate.update(sql, Constant.ACCOUNT_STATE_ENABLE, uid);
//	}
//
//	public String getUserNameByUid(long uid) {
//		String sql = "select name from account where uid = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		return list.size() > 0 ? list.get(0).get("name").toString() : "";
//	}
//
//	public long getUidByUsername(String user) {
//		String sql = "select uid from account where name = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, user);
//		return list.size() > 0 ? (Long) list.get(0).get("uid") : 0;
//	}
//
//	public String getMoneyForDepartment(long uid, int type) {
//		String sql = "select money from virtualCoinLimit where limitType = ? and departmentId = ?";
//		int departmentId = -1;
//		if (uid == Constant.WANGYOU_DEPARTMENT_LEADER_ID)
//			departmentId = Constant.WANGYOU_DEPARTMENT;
//		else if (uid == Constant.DANJI_DEPARTMENT_LEADER_ID)
//			departmentId = Constant.DANJI_DEPARTMENT;
//
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, type, departmentId);
//		return list.size() > 0 ? list.get(0).get("money").toString() : "";
//	}
//
//	public String getNotesForOperator(long uid) {
//		String sql = "select notes from accountForOperator where uid=? ";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		return list.size() > 0 ? list.get(0).get("notes").toString() : "";
//	}
//
//	public Set<String> getLoginAreaByUid(long uid) {
//		String sql = "select areaCode from loginLocation where uid=?";
//		Set<String> result = new HashSet<String>();
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		for (Map<String, Object> item : list) {
//			String areaCode = item.get("areaCode").toString();
//			result.add(areaCode);
//		}
//		return result;
//	}
//
//	public void addArea(long uid, String areaCode) {
//		String sql = "insert into loginLocation(uid,areaCode,createTimeStamp) values(?,?,?)";
//		this.jdbcTemplate.update(sql, uid, areaCode, System.currentTimeMillis());
//
//	}
//
//	public void updateArea(long uid, String oldCode, String newCode) {
//		String sql = "UPDATE loginLocation set areaCode = ?, createTimeStamp = ? where uid = ? and areaCode = ?";
//		this.jdbcTemplate.update(sql, newCode, System.currentTimeMillis(), uid, oldCode);
//	}
//
//	public boolean isOperator(long uid) {
//		String sql = "select count(1) from accountForOperator where uid = ?";
//		return this.jdbcTemplate.queryForInt(sql, uid) > 0;
//	}
//
//	public boolean isDeveloper(long uid) {
//		String sql = "select count(1) from developer where uid = ? and isDeveloper=?";
//		return this.jdbcTemplate.queryForInt(sql, uid, Constant.ACCOUNT_IS_DEVELOPER_TRUE) > 0;
//	}
//
//	public boolean isAuthorizedAccount(long uid) {
//		String sql = "select count(1) from accountAuthorizations where subUid = ? and enable=1";
//		return this.jdbcTemplate.queryForInt(sql, uid) > 0;
//	}
//
//	public boolean markDeveloper(long uid) {
//		String sql = "INSERT INTO developer (uid, isDeveloper, createTimeStamp ) values (?, ?, ?)  ON DUPLICATE KEY UPDATE isDeveloper=?;";
//		return this.jdbcTemplate.update(sql, uid, Constant.ACCOUNT_IS_DEVELOPER_TRUE, System.currentTimeMillis(),
//				Constant.ACCOUNT_IS_DEVELOPER_TRUE) > 0;
//	}
//
//	public Set<Long> getOperatorUids() {
//		String sql = "SELECT * FROM accountForOperator";
//		Set<Long> uids = new HashSet<Long>();
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
//		for (Map<String, Object> map : list) {
//			uids.add((Long) map.get("uid"));
//		}
//		return uids;
//	}
//
//	public int insertUserConnect(String tableName, String tableValue) {
//		if (this.jdbcTemplate.queryForLong("select count(1) from " + tableName) > 0) { // 表中已经有数据，将状态置为0
//			String updateSql = "update detailAccount set status = 0";
//			this.jdbcTemplate.update(updateSql);
//		}
//		String sql = "insert into detailAccount  "
//				+ "(uid, type, companyName, contact, bankAccount, bankAccountName, province, city, bank, subbranch, qq, email, tel) VALUES "
//				+ tableValue.substring(0, tableValue.length() - 2);
//		return this.jdbcTemplate.update(sql);
//	}
//
//	public boolean hasCharged(Long uid) {
//		String sql = "select balance from account where uid = ?";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uid);
//		if (list.size() > 0) // 充值过，则余额不为零
//			return new BigDecimal(list.get(0).get("balance").toString()).compareTo(BigDecimal.ZERO) > 0;
//		return false;
//	}
//
//	public boolean hasReceive(Long uid) {
//		// 已经领取体验金，则该字段为1
//		String sql = "select hasReceive from account where uid = ?";
//		return this.jdbcTemplate.queryForInt(sql, uid) > 0;
//	}
//
//	public List<Map<String, Object>> getAccountAuthorizations(Long uid) {
//		String sql = "select * from accountAuthorizations where superUid = ? and enable = ? order by createTimestamp desc";
//		return this.jdbcTemplate.queryForList(sql, uid, 1);
//	}
//
//	public int addAccountAuthorizations(Long superUid, String subUid, String gid) {
//		String querySql = "select * from accountAuthorizations where superUid = ? and subUid = ? and gid = ? and enable = ?";
//		if (this.jdbcTemplate.queryForList(querySql, superUid, subUid, gid, 1).size() > 0)
//			return 0;
//		Long time = System.currentTimeMillis();
//		String sql = "insert into accountAuthorizations(`superUid`, `subUid`, `gid`, `createTimestamp`, `enable`) values (?, ?, ?, ?, ?)";
//		return this.jdbcTemplate.update(sql, superUid, subUid, gid, time, 1);
//	}
//
//	public int delAccountAuthorizations(Long superUid, String subUid, String gid) {
//		String querySql = "select * from accountAuthorizations where superUid = ? and subUid = ? and gid = ? and enable = ?";
//		if (this.jdbcTemplate.queryForList(querySql, superUid, subUid, gid, 1).size() == 0)
//			return 0;
//		String sql = "update accountAuthorizations set enable = 0 where superUid = ? and subUid = ? and gid = ? and enable = ?";
//		return this.jdbcTemplate.update(sql, superUid, subUid, gid, 1);
//	}
//
//	@SuppressWarnings("unused")
//	public Long getUidBySearch(String userInfo) {
//		String sql = "select uid from account where ";
//		Long long1 = null;
//		try {
//			long1 = Long.parseLong(userInfo);
//			sql += " uid =  " + userInfo + " or name like '" + userInfo + "'";
//		} catch (Exception e) {
//			sql += " name like '" + userInfo + "'";
//		}
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
//		return list.size() > 0 ? (Long) list.get(0).get("uid") : 0L;
//	}
//
//	public int updateHasReceive(Long uid) {
//		String sql = "update account set hasReceive = ? where uid = ?";
//		return this.jdbcTemplate.update(sql, 1, uid);
//	}
	
	public Long getWXUid(String openId) {
		String sql = "select * from wx_user where openid = ? and state=1";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, openId);
		
		if(list.size() == 0) {
			if(bindWXInfo(openId) == 1){
				list = this.jdbcTemplate.queryForList(sql, openId);
				return list.size() > 0 ? (Long) list.get(0).get("id") : -1l;
				
			}
		}
		
		return list.size() > 0 ? (Long) list.get(0).get("id") : -1l;
	}
	
	public Integer bindWXInfo(String openId) {
		Long time = System.currentTimeMillis();
		String sql = "insert into wx_user(`openId`, `state`, `createTime`) values (?, ?, ?)";
		return this.jdbcTemplate.update(sql, openId , 1, time);
	}


	public static void main(String[] args) {
		// ApplicationContext ac = new
		// ClassPathXmlApplicationContext("classpath:spring/adgame_planquery.xml");
	}


}
