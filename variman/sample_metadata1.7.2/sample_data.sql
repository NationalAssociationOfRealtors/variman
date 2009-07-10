--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pgsql
--

COMMENT ON SCHEMA public IS 'Standard public schema';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: agents; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE agents (
    id integer NOT NULL,
    first_name character varying(12),
    last_name character varying(18),
    office_phone character varying(11),
    cell_phone character varying(11),
    url character varying(40)
);


ALTER TABLE public.agents OWNER TO variman;

--
-- Name: properties; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE properties (
    id integer NOT NULL,
    agent_id integer,
    street_number character varying(12),
    street_direction character varying(12),
    street_name character varying(24),
    city character varying(24),
    state character varying(24),
    zip character varying(10),
    county character varying(24),
    board character varying(24),
    sqft numeric,
    status character(1) NOT NULL,
    list_date date,
    list_price numeric,
    contract_date date,
    close_date date,
    close_price numeric,
    modification_timestamp timestamp without time zone
);


ALTER TABLE public.properties OWNER TO variman;

--
-- Name: commercial_properties; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE commercial_properties (
    building_number integer,
    building_name character varying(50),
    year_built integer,
    number_of_floors integer,
    tenency_type character varying(10)
)
INHERITS (properties);


ALTER TABLE public.commercial_properties OWNER TO variman;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: variman
--

CREATE SEQUENCE hibernate_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO variman;

--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: variman
--

SELECT pg_catalog.setval('hibernate_sequence', 3, true);


--
-- Name: residential_properties; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE residential_properties (
    rooms integer,
    garage integer,
    year_built integer,
    living_area integer,
    unit character varying(12),
    beds integer,
    baths integer
)
INHERITS (properties);


ALTER TABLE public.residential_properties OWNER TO variman;

--
-- Name: rets_accounting; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE rets_accounting (
    id bigint NOT NULL,
    totaltime bigint,
    userid bigint
);


ALTER TABLE public.rets_accounting OWNER TO variman;

--
-- Name: rets_group; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE rets_group (
    id bigint NOT NULL,
    name character varying(32) NOT NULL,
    description character varying(80)
);


ALTER TABLE public.rets_group OWNER TO variman;

--
-- Name: rets_user; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE rets_user (
    id bigint NOT NULL,
    firstname character varying(80) NOT NULL,
    lastname character varying(80) NOT NULL,
    username character varying(32) NOT NULL,
    passwordmethod character varying(255),
    "password" character varying(80),
    agentcode character varying(80),
    brokercode character varying(80)
);


ALTER TABLE public.rets_user OWNER TO variman;

--
-- Name: rets_user_groups; Type: TABLE; Schema: public; Owner: variman; Tablespace: 
--

CREATE TABLE rets_user_groups (
    group_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.rets_user_groups OWNER TO variman;

--
-- Data for Name: agents; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY agents (id, first_name, last_name, office_phone, cell_phone, url) FROM stdin;
1	Joe	Schmoe	3125551212	3125552121	http://JoeSchmoeRealty.com
\.


--
-- Data for Name: commercial_properties; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY commercial_properties (id, agent_id, street_number, street_direction, street_name, city, state, zip, county, board, sqft, status, list_date, list_price, contract_date, close_date, close_price, modification_timestamp, building_number, building_name, year_built, number_of_floors, tenency_type) FROM stdin;
8	1	875	N	Michigan Ave.	Chicago	IL	60611	Cook	ChicagoFakeCommercialMLS	325000	A	2009-02-12	125000000	\N	\N	\N	\N	\N	John Hancock Center	1970	100	Mixed
9	1	150	N	Michigan Ave.	Chicago	IL	60611	Cook	ChicagoFakeCommercialMLS	110000	A	2008-06-06	50000000	\N	\N	\N	\N	\N	Smurfit-Stone Building	1983	41	Office
10	1	1	N	Wacker Dr.	Chicago	IL	60606	Cook	ChicagoFakeCommercialMLS	200000	A	2009-03-15	5000000	\N	\N	\N	\N	\N	UBS Tower	1999	40	Office
7	1	233	S	Wacker Dr.	Chicago	IL	60606	Cook	ChicagoFakeCommercialMLS	350000	C	2008-11-12	100000000	\N	2009-05-01	95000000	\N	\N	Sears Tower	1974	110	Mixed
6	1	430	N	Michigan Ave	Chicago	IL	60611	Cook	ChicagoFakeCommercialMLS	40000	A	2009-01-01	5000000	\N	\N	\N	\N	\N	REALTOR Building	1963	12	Office
\.


--
-- Data for Name: properties; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY properties (id, agent_id, street_number, street_direction, street_name, city, state, zip, county, board, sqft, status, list_date, list_price, contract_date, close_date, close_price, modification_timestamp) FROM stdin;
\.


--
-- Data for Name: residential_properties; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY residential_properties (id, agent_id, street_number, street_direction, street_name, city, state, zip, county, board, sqft, status, list_date, list_price, contract_date, close_date, close_price, modification_timestamp, rooms, garage, year_built, living_area, unit, beds, baths) FROM stdin;
1	1	83	\N	Fox Hill	Buffalo Grove	IL	60089	Lake	TestMLS	4000	A	2008-05-30	380000	\N	\N	\N	\N	12	3	1985	2000	\N	4	3
2	1	1250	W	Armitage Ave	Chicago	IL	60613	Cook	FakeMLS	900	A	2009-03-23	250000	\N	\N	\N	\N	8	1	1975	900	22B	3	1
4	1	120	\N	Main St.	Evenston	IL	60533	Cook	TestMLS	3000	A	2008-10-03	425000	\N	\N	\N	\N	14	3	1922	2500	\N	5	3
5	1	5429	S	Harper Ave.	Chicago	IL	60615	Cook	FakeMLS	1000	A	2009-02-22	150000	\N	\N	\N	\N	5	\N	1915	1000	2N	1	1
3	1	625	N	Michigan Ave.	Chicago	IL	60611	Cook	FakeMLS	1100	C	2009-01-22	250000	\N	2009-04-15	233000	\N	9	2	1978	1100	6	4	3
\.


--
-- Data for Name: rets_accounting; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY rets_accounting (id, totaltime, userid) FROM stdin;
2	456934	1
\.


--
-- Data for Name: rets_group; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY rets_group (id, name, description) FROM stdin;
3	Users	
\.


--
-- Data for Name: rets_user; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY rets_user (id, firstname, lastname, username, passwordmethod, "password", agentcode, brokercode) FROM stdin;
1	Joe	Schmoe	Joe	A1:RETS Server	a04aab7e749c8f532b78bdd61dfa0cb2		
\.


--
-- Data for Name: rets_user_groups; Type: TABLE DATA; Schema: public; Owner: variman
--

COPY rets_user_groups (group_id, user_id) FROM stdin;
3	1
\.


--
-- Name: agents_pkey; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY agents
    ADD CONSTRAINT agents_pkey PRIMARY KEY (id);


--
-- Name: listings_pkey; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY properties
    ADD CONSTRAINT listings_pkey PRIMARY KEY (id);


--
-- Name: rets_accounting_pkey; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY rets_accounting
    ADD CONSTRAINT rets_accounting_pkey PRIMARY KEY (id);


--
-- Name: rets_group_name_key; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY rets_group
    ADD CONSTRAINT rets_group_name_key UNIQUE (name);


--
-- Name: rets_group_pkey; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY rets_group
    ADD CONSTRAINT rets_group_pkey PRIMARY KEY (id);


--
-- Name: rets_user_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY rets_user_groups
    ADD CONSTRAINT rets_user_groups_pkey PRIMARY KEY (user_id, group_id);


--
-- Name: rets_user_pkey; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY rets_user
    ADD CONSTRAINT rets_user_pkey PRIMARY KEY (id);


--
-- Name: rets_user_username_key; Type: CONSTRAINT; Schema: public; Owner: variman; Tablespace: 
--

ALTER TABLE ONLY rets_user
    ADD CONSTRAINT rets_user_username_key UNIQUE (username);


--
-- Name: fk21447ffb1e2e76db; Type: FK CONSTRAINT; Schema: public; Owner: variman
--

ALTER TABLE ONLY rets_user_groups
    ADD CONSTRAINT fk21447ffb1e2e76db FOREIGN KEY (group_id) REFERENCES rets_group(id);


--
-- Name: fk21447ffbf73aee0f; Type: FK CONSTRAINT; Schema: public; Owner: variman
--

ALTER TABLE ONLY rets_user_groups
    ADD CONSTRAINT fk21447ffbf73aee0f FOREIGN KEY (user_id) REFERENCES rets_user(id);


--
-- Name: fkb95eb102ce2b2e46; Type: FK CONSTRAINT; Schema: public; Owner: variman
--

ALTER TABLE ONLY rets_accounting
    ADD CONSTRAINT fkb95eb102ce2b2e46 FOREIGN KEY (userid) REFERENCES rets_user(id);


--
-- Name: listings_agent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: variman
--

ALTER TABLE ONLY properties
    ADD CONSTRAINT listings_agent_id_fkey FOREIGN KEY (agent_id) REFERENCES agents(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: pgsql
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM pgsql;
GRANT ALL ON SCHEMA public TO pgsql;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: agents; Type: ACL; Schema: public; Owner: variman
--

REVOKE ALL ON TABLE agents FROM PUBLIC;
REVOKE ALL ON TABLE agents FROM variman;
GRANT ALL ON TABLE agents TO variman;


--
-- PostgreSQL database dump complete
--

