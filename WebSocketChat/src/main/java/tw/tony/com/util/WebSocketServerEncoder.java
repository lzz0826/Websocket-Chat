package tw.tony.com.util;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.tony.com.model.Chat;

public class WebSocketServerEncoder implements Encoder.Text<Chat> {

	@Override
	public void init(EndpointConfig endpointConfig) {

	}

	@Override
	public void destroy() {

	}

	@Override
	public String encode(Chat cjat) throws EncodeException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(cjat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
