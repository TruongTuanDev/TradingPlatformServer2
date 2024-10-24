package services;

import java.util.List;

import model.Token;
import repositorys.TokenRepository;

public class TokenService {
	private Token token;
	private TokenRepository tokenRepository;
	public Token createToken(String name,String symbol,Double current_price,String date,double marketcap,double quantity) {
		  token = new Token();
		  tokenRepository = new TokenRepository();
		  token.setName(name);
		  token.setSymbol(symbol);
	      token.setCurrent_price(current_price);
	      token.setDate(date);
	      token.setMarketcap(marketcap);
	      token.setQuantity(quantity);
	      token = tokenRepository.save(token);
	      return token;
	 }
	public List<Token> getListTokens() {
	    tokenRepository = new TokenRepository();
	    return tokenRepository.findAllTokens();
	}
	public List<Token> searchTokensByKeyword(String keyword) {
	    tokenRepository = new TokenRepository();
	    return tokenRepository.findTokenByKeyword(keyword);
	}


}
