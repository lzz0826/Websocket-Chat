package tw.tony.com.mapper;

import org.apache.ibatis.annotations.Mapper;

import tw.tony.com.model.MessageLog;



@Mapper
public interface MessageLogMapper {
	
	void addMessageLog(MessageLog messageLog);
	

}
