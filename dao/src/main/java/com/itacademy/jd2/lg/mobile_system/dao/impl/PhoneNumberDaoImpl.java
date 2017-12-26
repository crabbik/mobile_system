package com.itacademy.jd2.lg.mobile_system.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itacademy.jd2.lg.mobile_system.dao.IPhoneNumberDao;
import com.itacademy.jd2.lg.mobile_system.dao.dbmodel.PhoneNumber;
import com.itacademy.jd2.lg.mobile_system.dao.exception.SQLExecutionExecption;

public class PhoneNumberDaoImpl extends AbstractDaoImpl implements IPhoneNumberDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhoneNumberDaoImpl.class);
	public static final IPhoneNumberDao PHONENUMBER_DAO = new PhoneNumberDaoImpl();

	private PhoneNumberDaoImpl() {
		super();
	}

	public PhoneNumber get(Integer id) {
		return this.<PhoneNumber>executeWithConnection(new DBAction<PhoneNumber>() {

			@Override
			public PhoneNumber execute(Connection c, Statement stmt) throws SQLException {
				PhoneNumber phoneNumber = null;
				String sqlGet = "select * from phone_number where id=" + id;
				ResultSet rs = stmt.executeQuery(sqlGet);
				LOGGER.debug("created ResultSet");
				if (rs.next()) {
					phoneNumber = mapToPhoneNumber(rs);
					LOGGER.debug("read phoneNumber from the database: {}", phoneNumber.toString());
				}
				rs.close();
				LOGGER.debug("ResultSet closed");
				LOGGER.info("return phoneNumber from db: {}", phoneNumber);
				return phoneNumber;
			}

		});

	}

	@Override
	public int insert(PhoneNumber phoneNumber) {
		String sqlInsert = "insert into phone_number (id,account_id, number, tariff_id, created, modified) values (?,?,?,?,?,?)";
		LOGGER.debug("insert SQL:{}", sqlInsert);
		try (Connection c = getConnection();
				PreparedStatement preparedStatement = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, phoneNumber.getId());
			preparedStatement.setInt(2, phoneNumber.getAccountId());
			preparedStatement.setString(3, phoneNumber.getNumber());
			preparedStatement.setInt(4, phoneNumber.getTariffId());
			preparedStatement.setTimestamp(5, phoneNumber.getCreated());
			preparedStatement.setTimestamp(6, phoneNumber.getModified());
			preparedStatement.executeUpdate();
			LOGGER.info("insert phoneNumber from db:{}", phoneNumber);
			ResultSet rs = preparedStatement.getGeneratedKeys();
			LOGGER.debug("created ResulSet");
			rs.next();
			int id = rs.getInt("id");
			LOGGER.debug("return generated key {}", id);
			rs.close();
			LOGGER.debug("ResulSet closed");
			return id;
		} catch (Exception e) {
			throw new SQLExecutionExecption(e);
		}
	}

	@Override
	public void update(PhoneNumber phoneNumber) {
		String sqlUpdate = "update phone_number set account_id=?, number=?, tariff_id=?, modified=? where id=?";
		LOGGER.debug("update SQL: {}", sqlUpdate);
		try (Connection c = getConnection(); PreparedStatement preparedStatement = c.prepareStatement(sqlUpdate)) {
			preparedStatement.setInt(1, phoneNumber.getAccountId());
			preparedStatement.setString(2, phoneNumber.getNumber());
			preparedStatement.setInt(3, phoneNumber.getTariffId());
			preparedStatement.setTimestamp(4, phoneNumber.getModified());
			preparedStatement.setInt(5, phoneNumber.getId());
			preparedStatement.executeUpdate();
			LOGGER.info("update phoneNumber:{}", phoneNumber);
		} catch (Exception e) {
			throw new SQLExecutionExecption(e);
		}
	}

	@Override
	public List<PhoneNumber> getAll() {
		return this.<List<PhoneNumber>>executeWithConnection(new DBAction<List<PhoneNumber>>() {

			@Override
			public List<PhoneNumber> execute(Connection c, Statement stmt) throws SQLException {
				String sqlGetAll = "select * from phone_number";
				LOGGER.debug("get all phone_number SQL:{}", sqlGetAll);
				List<PhoneNumber> listPhoneNumber = sqlGetAllPhoneNumber(sqlGetAll, stmt);
				LOGGER.info("received a list of data from the database:{}", listPhoneNumber);
				return listPhoneNumber;
			}

		});

	}

	private PhoneNumber mapToPhoneNumber(ResultSet rs) throws SQLException {
		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setId(rs.getInt("id"));
		phoneNumber.setAccountId(rs.getInt("account_id"));
		phoneNumber.setNumber(rs.getString("number"));
		phoneNumber.setTariffId(rs.getInt("tariff_id"));
		phoneNumber.setCreated(rs.getTimestamp("created"));
		phoneNumber.setModified(rs.getTimestamp("modified"));
		return phoneNumber;
	}

	@Override
	protected String getTableName() {
		String tableName = "phone_number";
		LOGGER.debug("return table name to remove data:{}", tableName);
		return tableName;
	}

	@Override
	public List<PhoneNumber> getAll(int limit, int offset) {
		return this.<List<PhoneNumber>>executeWithConnection(new DBAction<List<PhoneNumber>>() {

			@Override
			public List<PhoneNumber> execute(Connection c, Statement stmt) throws SQLException {
				String sqlGetAll = String.format("select * from phone_number limit %s offset %s", limit, offset);
				LOGGER.debug("get all phoneNumber SQL:{}", sqlGetAll);
				List<PhoneNumber> listPhoneNumber = sqlGetAllPhoneNumber(sqlGetAll, stmt);
				LOGGER.info("received a list of data from the database:{}", listPhoneNumber);
				return listPhoneNumber;
			}

		});

	}

	private List<PhoneNumber> sqlGetAllPhoneNumber(String sql, Statement stmt) throws SQLException {
		List<PhoneNumber> listPhoneNumber = new ArrayList<>();
		ResultSet rs = stmt.executeQuery(sql);
		LOGGER.debug("created ResulSet");
		while (rs.next()) {
			PhoneNumber phoneNumber = mapToPhoneNumber(rs);
			LOGGER.debug("read phoneNumber from the database: {}", phoneNumber);
			listPhoneNumber.add(phoneNumber);
			LOGGER.debug("phoneNumber added from list");
		}
		rs.close();
		LOGGER.debug("ResulSet closed");
		return listPhoneNumber;
	}

	@Override
	protected String getIdName() {
		String idName = "id";
		LOGGER.debug("return id name to remove data:{}", idName);
		return idName;
	}

}