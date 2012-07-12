package converter;

enum FrameNetLayerName {
	Target,
	FE,
	GF {
		public String correspondingFeatureDomain() {
			return FeatureDomain.NT.toString();
		}
	},
	PT {
		public String correspondingFeatureDomain() {
			return FeatureDomain.NT.toString();
		}
	},
	Other {
		public String correspondingFeatureDomain() {
			return FeatureDomain.T.toString();
		}
	},
	Verb,
	Sent,
	BNC;
	
	static enum FeatureDomain {
		T, NT, FREC
	}
	
	String correspondingFeatureDomain() {
		return null;
	}
}
