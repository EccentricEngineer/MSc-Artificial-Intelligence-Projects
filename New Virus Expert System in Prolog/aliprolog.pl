% Dynamic inputs of the expert system are defined below with appropriate variables and arguments.

:-dynamic(p_data/2).
 
age_input(Y):-
    (   p_data(age,Y),! );
    (   write("What is the age of the patient? (0 <= x <= 100)"),nl,
        read(Y),nl,
        assert(p_data(age,Y))
    ).

gender_input(X):-
    (   p_data(gender,X),! );
    (   write("What is the gender of the patient? (male/female)"),nl,
        read(X),nl,
        assert(p_data(gender,X))
    ).
 
incubationperiod_input(D):-
    (   p_data(incubationperiod,D),! );
    (   write("What is the incubation period (time between exposure to virus and onset of symptoms)? (0 <= x <= 100)"),nl,
        read(D),nl,
        assert(p_data(incubationperiod,D))
    ).
 
symptoms_input(Z):-
    (   p_data(symptoms,Z),! );
    (   write("Please input your symptom."),nl,
        read(Z),nl,
        assert(p_data(symptoms,Z))
    ).

preexistingcondition_input(C):-
    (   p_data(preexistingcondition,C),! );
    (   write("Please input any of your pre-existing health condition. (Write no if none)"),nl,
        read(C),nl,
        assert(p_data(preexistingcondition,C))
    ).

% Defining the consultdoctor predicate and its arguments which represent serious symptoms.

consultdoctor(chestpain).
consultdoctor(chestpressure).
consultdoctor(loss_of_speech_or_movement).
consultdoctor(breathingproblem).

% Common symptoms predicate and its arguments.

infectionpresent(fever).
infectionpresent(dry_cough).
infectionpresent(tiredness).


% Less common symptoms predicate and its arguments.

infectionpresent(conjunctivitis).
infectionpresent(sorethroat).
infectionpresent(diarrhoea).
infectionpresent(pains).
infectionpresent(headache).
infectionpresent(anosmia).
infectionpresent(runningnose).


%  Pre-existing health condition predicate and its arguments.

seriousexistingcondition(diabetes).
seriousexistingcondition(hypertension).
seriousexistingcondition(cardiovasculardisease).
seriousexistingcondition(chronicrespiratorydisease).
seriousexistingcondition(cancer).

% The rules for above facts are written below with predicates defined in the body and variables defined in the head.

message_for_age_gdr_prec(Age, Gender, Precondition):-
    Age >=70, write('High chance of severe infection due to old age'), nl;
    Gender = 'male', write('High chance of severe infection as males are more vunerable.'), nl;
    seriousexistingcondition(Precondition), write('High chance of severe infection.'), nl.

message_for_infection(IncubationPeriod, Symptom):-
    IncubationPeriod > 14, write('Low chances of Virus'), nl;
    infectionpresent(Symptom), not(consultdoctor(Symptom)), write('This is a symptom of new virus. Stay at home and monitor your symptoms.'), nl;
    consultdoctor(Symptom), write('See a medical professional.'), nl.

% The call function parameters are defined below.

checkAll(Message):-
    age_input(Age),
    gender_input(Gender),
    incubationperiod_input(IncubationPeriod),
    symptoms_input(Symptom),
    preexistingcondition_input(Precondition),
    message_for_age_gdr_prec(Age, Gender, Precondition),
    message_for_infection(IncubationPeriod, Symptom),
    Message = 'done'.

retractal.