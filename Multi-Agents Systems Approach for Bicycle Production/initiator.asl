/* Initial beliefs and rules */
// Check if proposals from all agents have been received
all_proposals_received(CNPId,NP) :- .count(propose(CNPId,_)[source(_)], NO) &   // number of proposals received
									.count(refuse(CNPId)[source(_)], NR) &      // number of refusals received
									 NP = NO + NR.								// NP = Number of proposals

/* Initial goal */
!register.
!assemble.

/* Plans */
// Register agent to the Directory Facilitator
+!register <- .df_register(initiator).

// In the event that the robotic arm agent has to move to the bin
// Under the circumstance that the RA is not already at the bin's location
// Recursively move the RA until it reaches the bin
+!moveToBin : not pos(robot, 0, 0) <- move_towards(0, 0, 0);
									 !moveToBin.
+!moveToBin.

// In the event that the RA has to move to a specific position
// Under the circumstance that the RA is not already present at the location
// Recursively move the RA until it reaches the specified location				
+!moveToPos(X, Y) : not pos(robot, X, Y) <- move_towards(X, Y, 0);
										   !moveToPos(X, Y).

+!moveToPos(_,_).

// Assemble pieces to build up the bike frame
+!assemble : not empty(bin) <- !cnp(1, bring_part123 , LP, "robotic_arm_agent");			// Request RA
								add(ra);													// Add RA to the environment
								pick(head);													// Pick Head Tube
							   !moveToPos(8, 1);
								drop(head);													// Drop Head Tube onto assembly surface
							   !moveToBin;													// Go back to the bin
								pick(top);													// Pick Top Tube
							   !moveToPos(5, 1);
								drop(top);													// Drop Top Tube onto assembly surface
							   !moveToBin;
								pick(down);													// Pick Down Tube
							   !moveToPos(6, 3);
								drop(down);													// Drop Down Tube onto assembly surface
							   .wait(1000);
								remove(ra);													// Remove RA from environment as it is currently not required
							   !cnp(2, hold_part1 , LP1, "holding_agent");					// Request a holding agent to hold the Head Tube
								add(h1);													// Add the first holding agent to the environment
							   !cnp(3, hold_part2 , LP2, "holding_agent");					// Request a holding agent to hold the Top Tube
								add(h2);                                                    // Add the second holding agent
							   !cnp(4, hold_part3 , LP3, "holding_agent");					// Request a holding agent to hold the Down Tube
								add(h3);													// Add the third holding agent
							   !cnp(5, weld_parts123 , LP4, "welding_agent");				// Request a welding agent to weld joint (1, 2, 3)
								add(w1);													// Add the welding agent to the environment
							   .wait(2000);
							   .print("Welding of head, top and bottom completed!");
							    remove(w1);													// Remove welding agent from environment
								remove(h1);													// Remove holding agents from environment
								remove(h2);
								remove(h3);
							   !cnp(6, bring_part46 , LP5, "robotic_arm_agent");				// Request RA
							    add(ra);													// Add RA back to the environment
								pick(seat);													// Pick Seat Tube
							   !moveToPos(3, 3);
								drop(seat);													// Drop Seat Tube onto assembly surface
							   !moveToBin;
							    pick(chain);												// Pick Chain Tube
							   !moveToPos(3, 5);
							    drop(chain);												// Drop Chain Tube onto assembly surface
							   .wait(1000);
							    remove(ra);													// Remove RA from environment
							   !cnp(7, hold_part3 , LP6, "holding_agent");					// Request a holding agent to hold the Down Tube
								add(h3);													// Add the first holding agent to the environment
							   !cnp(8, hold_part4 , LP7, "holding_agent");					// Request a holding agent to hold the Seat Tube
								add(h4);													// Add the second holding agent
							   !cnp(9, hold_part6 , LP8, "holding_agent");					// Request a holding agent to hold the Chain Stay
								add(h6);													// Add the third holding agent
							   !cnp(10, weld_parts346 , LP9, "welding_agent");				// Request a welding agent to weld joint (3, 4, 6)
								add(w2);													// Add the welding agent to the environment
							   .wait(2000);
							   .print("Welding of chain completed!");
							    remove(w2);													// Remove agents from environment
								remove(h3);
								remove(h4);
								remove(h6);
							   !cnp(11, hold_part2 , LP10, "holding_agent");				// Request a holding agent to hold the Top Tube
								add(h2);													
							   !cnp(12, hold_part4 , LP11, "holding_agent");				// Request a holding agent to hold the Seat Tube
								add(h4);
							   !cnp(13, weld_parts24 , LP12, "welding_agent");				// Request a welding agent to weld joint (2, 4)
								add(w3);
							   .wait(2000);
							   .print("Welding of seat with frame completed!");
							    remove(w3);
								remove(h2);
								remove(h4);
							   !cnp(14, bring_part5 , LP13, "robotic_arm_agent");			// Request RA agent to pick up last part
							    add(ra);
								pick(seatstay);												// Pick Seat Stay Tube
							   !moveToPos(1, 3);
								drop(seatstay);												// Drop Seat Stay Tube onto assembly surface
							   .wait(1000);
							    remove(ra);													// Remove RA from environment
							   !cnp(15, hold_part5 , LP14, "holding_agent");				// Request a holding agent to hold the Seat Stay
								add(h5);
							   !cnp(16, hold_part6 , LP15, "holding_agent");				// Request a holding agent to hold the Chain Stay
								add(h6);
							   !cnp(17, weld_parts56 , LP16, "welding_agent");				// Request a welding agent to weld the joint (5, 6)
								add(w4);
							   .wait(2000);
							   .print("Welding of seat stay with chain completed!");
							    remove(w4);
								remove(h6);
							   !cnp(18, hold_part4 , LP17, "holding_agent");				// Request a holding agent to hold the Seat Tube - already welded to bike frame
								add(h4);
							   !cnp(19, weld_parts45 , LP18, "welding_agent");				// Request a welding agent to weld the joint (4, 5)
								add(w5);
								weld(seatstay);
							   .wait(2000);
							   .print("Welding of seat stay with frame completed");
							    remove(w5);													// Remove all holding and welding agents from the environment
								remove(h5);
								remove(h4);
							   .print("Assembly of bike frame completed").

// Bike assembly post frame completion
+!assemble : complete(bike) <- !cnp(20, move_frame , LP19, "moving agent");					// Request a moving agent
							   .print("***** BIKE FRAME ASSEMBLY COMPLETED *****").

// Create the contract net protocol (CNP)
+!cnp(Id, Task, LP, Agent) <- !call(Id, Task , LP, Agent);									// Initiating the request
							  !bid(Id, LP);													// Bid from participants
							  !winner(Id, LO, WAg);											// Choosing a participant
							  !result(Id, LO, WAg).											// Declaring the result of the CNP

// Call for CNP proposal
+!call(Id, Task, LP, Agent) <- .print("Waiting participants for task ", Task, "...");
							   .wait(2000);  												// Wait for participants' introduction
							   .df_search(Agent, LP);
							   .print("Sending CFP to ", LP);								
							   .send(LP, tell, cfp(Id, Task)).								// Send call for proposal

+!call(_,_,_,_).

// The deadline of the CNP is now + 4 seconds (or all proposals received)					  
+!bid(Id, LP) <- .wait(all_proposals_received(Id, .length(LP)), 2000, _).

// An offer has been given to a participant
+!winner(Id, LO, WAg): .findall(offer(O,A),propose(CNPId,O)[source(A)],LO) & LO \== [] <- .print("Offers are ", LO);
																						  .min(LO,offer(WOf, WAg)); 				// the first offer is the best
																						  .print("Winner is ", WAg," with ", WOf).

+!winner(_,_,nowinner). // no offer case

// Announce result to the winner
+!result(CNPId,[offer(_,WAg)|T], WAg) <- .send(WAg, tell, accept_proposal(CNPId));
										 !result(CNPId, T, WAg).

// Announce  result to other participatns
+!result(CNPId,[offer(_,LAg)|T],WAg) <- .send(LAg, tell, reject_proposal(CNPId));
										!result(CNPId, T, WAg).
+!result(_,[],_).