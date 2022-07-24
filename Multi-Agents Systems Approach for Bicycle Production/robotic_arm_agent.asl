/* Initial goal */
!register.

/* Plans */
// Register the RA agent to the Directory Facilitator
+!register <- .df_register("robotic_arm_agent");
              .df_subscribe("initiator").
			  
// Answer to Call For Proposal
@c1 +cfp(CNPId,Task)[source(A)]: provider(A,"initiator") <- +proposal(CNPId, Task, Offer); 			// remember my proposal
															.send(A, tell, propose(CNPId, Offer)).

// Do the task and report to initiator															
@r1 +accept_proposal(CNPId): proposal(CNPId,Task,Offer) <- .print("Picking parts for ", Task).

// Proposal was not accepted by initiator
@r2 +reject_proposal(CNPId) <- -proposal(CNPId,_,_). // clear memory