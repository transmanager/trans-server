package channy.transmanager.shaobao.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import channy.transmanager.shaobao.data.user.RoleDao;
import channy.transmanager.shaobao.feature.Action;
import channy.util.ChannyException;
import channy.util.ErrorCode;
import channy.util.JsonResponse;

@Controller
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@RequestMapping(value = "/role/query", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String querySelect(@RequestParam("action") String action, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
			@RequestParam("q") String term , HttpServletRequest request) throws JSONException, XPathExpressionException, SAXException, IOException, ParserConfigurationException, ChannyException {
		if (Action.valueOf(Action.class, action) != Action.RoleQuery) {
			return new JsonResponse(ErrorCode.BAD_REQUEST_CODE).generate();
		}
		
		JSONObject data = RoleDao.querySelect(page, pageSize, term);
		return new JsonResponse(ErrorCode.OK, data).generate();
	}
}
