package com.neocoretechs.relatrix;

import java.io.Serializable;
import java.util.Objects;
/**
 * Wrap the rocksack alias to abstract it away.
 * An alias is the means by which we can designate different databases when obtaining our ordered maps from the {@link com.neocoretechs.rocksack.session.DatabaseManager}.<p>
 * Recall that in RockSack, a database is a tablespace directory that contains class instances which are RocksDB directories containing
 * the serialized object instances. An alias allows us to define a macro that indicates the set of class instances that make up a database. For instance; 
 * If we have a tablespace directory of /home/db, then we can define a database as /home/db/test.<p>
 * Instances of serialized classes will then be created under /home/db/testjava.lang.String,
 * /home/db/testcom.yourpackage.yourclass subdirectories that contain the RocksDB SST and log files. etc. <p>
 * If we define an alias as:<br>
 * Alias testDb = new Alias("test");<br>
 * we can then call:<br>
 * DatabaseManager.setTableSpaceAlias(testDb, "/home/db/"),
 * and thereafter refer to testDb when obtaining and subsequently operating on a {@link com.neocoretechs.rocksack.session.BufferedMap}
 * or {@link com.neocoretechs.rocksack.session.TransactionalMap}, and the data for those maps will be placed under our /home/db/test...
 * series of subdirectories.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024,2025
 *
 */
public class Alias implements Serializable {
	private static final long serialVersionUID = -4900917167930271807L;
	private String alias;
	public Alias() {}
	public Alias(String alias) {
		this.alias = alias;
	}
	public String getAlias() {
		return alias;
	}
	public com.neocoretechs.rocksack.Alias getRocksackAlias() {
		return new com.neocoretechs.rocksack.Alias(alias);
	}
	@Override
	public String toString() {
		return alias;
	}
	@Override
	public int hashCode() {
		return Objects.hash(alias);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Alias)) {
			return false;
		}
		Alias other = (Alias) obj;
		return Objects.equals(alias, other.alias);
	}

}
