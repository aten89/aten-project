/**
 * 
 */
package org.eapp.rpc.session;

import org.eapp.hbean.ActorAccount;

/**
 * @author linliangyi 2008-06-21
 * @version 1.0
 */
public class SimpleCredenceAlgorithm implements ICredenceAlgorithm {

	@Override
	public boolean verifyCredence(ActorAccount actor, String targetCredence) {
		if(actor == null || targetCredence == null){
			return false;
		}
		String sourceCredence = actor.getCredence();
		if(sourceCredence == null){
			return false;
		}
		return new String(sourceCredence).trim().equals(new String(targetCredence));
		
	}

}
