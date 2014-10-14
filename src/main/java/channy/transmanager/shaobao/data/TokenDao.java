package channy.transmanager.shaobao.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import channy.transmanager.shaobao.data.user.UserDao;
import channy.transmanager.shaobao.model.Token;
import channy.transmanager.shaobao.model.user.User;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.HibernateUtil;

public class TokenDao {
	public static boolean exists(User user) {
		return getByUser(user) != null;
	}

	public static Token getByUser(User user) {
		// Session session = HibernateUtil.openSession();
		// try {
		// session.beginTransaction();
		// Query query =
		// session.createQuery("from Token token where token.user = :user");
		// query.setParameter("user", user);
		// @SuppressWarnings("unchecked")
		// List<Token> result = query.list();
		// session.getTransaction().commit();
		// if (result.size() == 0) {
		// return null;
		// } else {
		// return result.get(0);
		// }
		// } finally {
		// HibernateUtil.closeSession(session);
		// }

		Session session = HibernateUtil.getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("from Token token where token.user = :user");
		query.setParameter("user", user);
		@SuppressWarnings("unchecked")
		List<Token> result = query.list();
		session.getTransaction().commit();
		if (result.size() == 0) {
			return null;
		} else {
			return result.get(0);
		}
	}

	public static void add(String token, User user) throws ChannyException {
		if (exists(user)) {
			throw new ChannyException(ErrorCode.OBJECT_EXISTED);
		}
		Token tp = new Token();
		tp.setToken(token);
		tp.setUser(user);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 5);
		tp.setExpiration(c.getTime());

		HibernateUtil.persist(tp);
	}

	public static void update(Token token) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 5);
		token.setExpiration(c.getTime());
		HibernateUtil.update(token);
	}

	public static ErrorCode authenticate(User user, String token) {
		if (user == null) {
			return ErrorCode.UNAUTHORIZED;
		}

		Token tp = TokenDao.getByUser(user);
		if (tp == null) {
			return ErrorCode.UNAUTHORIZED;
		}

		if (!tp.getToken().equals(token)) {
			return ErrorCode.BAD_TOKEN;
		}

		Date current = new Date();
		Date lastLogin = tp.getExpiration();
		if (current.getTime() > lastLogin.getTime()) {
			return ErrorCode.SESSION_EXPIRED;
		}

		return ErrorCode.OK;
	}

	public static void main(String[] args) throws ChannyException {
		// User user = UserDao.getByEmployeeId("10001");
		// Token token = getByUser(user);
		// // System.out.println(TokenDao.exists(user));
		// //add(UUID.randomUUID().toString(), user);
		// //authenticate(user, UUID.randomUUID().toString());
		// update(token);
	}
}
