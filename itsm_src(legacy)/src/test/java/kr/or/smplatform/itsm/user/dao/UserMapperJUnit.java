package kr.or.smplatform.itsm.user.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.go.smplatform.itsm.user.dao.UserMapper;
import kr.go.smplatform.itsm.user.vo.UserVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/egovframework/spring/context-aspect.xml"
		, "/egovframework/spring/context-common.xml"
		, "/egovframework/spring/context-idgen.xml"
		, "/egovframework/spring/context-datasource.xml"
		, "/egovframework/spring/context-properties.xml"
//		, "/egovframework/spring/context-sqlMap.xml"
		, "/egovframework/spring/context-mapper.xml"
		, "/egovframework/spring/context-transaction.xml"
})
public class UserMapperJUnit {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserMapperJUnit.class);
	
	@Resource(name="userMapper")
	private UserMapper userMapper;
	
	@Test
	public void testInit(){
		Assert.assertNotNull(userMapper);
	}
	
	@Test
	public void testRetrievePagingList(){
		List<UserVO> list = null;
		try {
			list = userMapper.retrievePagingList(new UserVO());
			for(UserVO userVO:list){
				LOGGER.info(userVO.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(list);
	}
}
