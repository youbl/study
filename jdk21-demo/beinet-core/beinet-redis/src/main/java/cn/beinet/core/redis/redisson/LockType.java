package cn.beinet.core.redis.redisson;

/**
 * 锁类型
 */
public enum LockType {
	/**
	 * 重入锁
	 */
	REENTRANT,
	/**
	 * 公平锁
	 */
	FAIR
}
