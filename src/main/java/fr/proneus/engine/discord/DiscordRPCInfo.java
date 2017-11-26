package fr.proneus.engine.discord;

public class DiscordRPCInfo {

	private String state;
	private String details;
	private long startTimestamp;
	private long endTimestamp;
	private String largeImageKey;
	private String largeImageText;
	private String smallImageKey;
	private String smallImageText;
	private String partyId;
	private int partySize;
	private int partyMax;
	private String matchSecret;
	private String joinSecret;
	private String spectateSecret;
	private byte instance = 1;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDetails() {
		return details;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp / 1000;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp / 1000;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setLargeImage(String largeImageKey, String largeImageText) {
		this.largeImageKey = largeImageKey;
		this.largeImageText = largeImageText;
	}

	public String getLargeImageKey() {
		return largeImageKey;
	}

	public String getLargeImageText() {
		return largeImageText;
	}

	public void setSmallImage(String smallImageKey, String smallImageText) {
		this.smallImageKey = smallImageKey;
		this.smallImageText = smallImageText;
	}

	public String getSmallImageKey() {
		return smallImageKey;
	}

	public String getSmallImageText() {
		return smallImageText;
	}

	public void setParty(String partyId, int partySize, int partyMax) {
		this.partyId = partyId;
		this.partySize = partySize;
		this.partyMax = partyMax;
	}

	public String getPartyId() {
		return partyId;
	}

	public int getPartySize() {
		return partySize;
	}

	public int getPartyMax() {
		return partyMax;
	}

	public void setMatchSecret(String matchSecret) {
		this.matchSecret = matchSecret;
	}

	public String getMatchSecret() {
		return matchSecret;
	}

	public void setJoinSecret(String joinSecret) {
		this.joinSecret = joinSecret;
	}

	public String getJoinSecret() {
		return joinSecret;
	}

	public void setSpectateSecret(String spectateSecret) {
		this.spectateSecret = spectateSecret;
	}

	public String getSpectateSecret() {
		return spectateSecret;
	}

	public void setInstance(byte instance) {
		this.instance = instance;
	}

	public byte getInstance() {
		return instance;
	}

}
