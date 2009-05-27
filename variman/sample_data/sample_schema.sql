--
-- PostgreSQL database dump
--

SET client_encoding = 'SQL_ASCII';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: actagt; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE actagt (
    office_id character varying(7),
    agent_id character varying(7) NOT NULL,
    r_md timestamp without time zone
);


ALTER TABLE public.actagt OWNER TO postgres;

--
-- Name: agt; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE agt (
    cell_phone character varying(11),
    office_id character varying(7),
    first_name character varying(12),
    agent_id character varying(7) NOT NULL,
    last_name character varying(18),
    dsgnations character varying(20),
    url character varying(40),
    off_phone character varying(11),
    "password" character varying(12),
    r_md timestamp without time zone
);


ALTER TABLE public.agt OWNER TO postgres;

--
-- Name: con; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE con (
    "owner" character varying(20),
    stnum character varying(10),
    ld date,
    broker character varying(40),
    vew bigint,
    ltyp character varying(4),
    status character varying(1),
    sqft character varying(8),
    m_school integer,
    h_school integer,
    stname character varying(20),
    lp integer,
    lif character varying(100),
    e_school integer,
    ar character varying(5),
    ln character varying(40) NOT NULL,
    ef character varying(100),
    url character varying(40),
    zip_code bigint,
    agent_id character varying(6),
    r_md timestamp without time zone
);


ALTER TABLE public.con OWNER TO postgres;

--
-- Name: lnd; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE lnd (
    vew bigint,
    sqft character varying(8),
    zip_code bigint,
    ln character varying(40) NOT NULL,
    ld date,
    ltyp character varying(4),
    ar character varying(5),
    agent_id character varying(6),
    broker character varying(40),
    lp integer,
    "owner" character varying(20),
    url character varying(40),
    m_school integer,
    lif character varying(100),
    ef character varying(100),
    e_school integer,
    h_school integer,
    stname character varying(20),
    status character varying(1),
    stnum character varying(10),
    r_md timestamp without time zone
);


ALTER TABLE public.lnd OWNER TO postgres;

--
-- Name: mob; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mob (
    ld date,
    stnum character varying(10),
    "owner" character varying(20),
    ltyp character varying(4),
    h_school integer,
    sqft character varying(8),
    zip_code bigint,
    ef character varying(100),
    ar character varying(5),
    ln character varying(40) NOT NULL,
    lif character varying(100),
    status character varying(1),
    stname character varying(20),
    e_school integer,
    m_school integer,
    url character varying(40),
    agent_id character varying(6),
    lp integer,
    vew bigint,
    broker character varying(40),
    r_md timestamp without time zone
);


ALTER TABLE public.mob OWNER TO postgres;

--
-- Name: mul; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mul (
    ln character varying(40) NOT NULL,
    lif character varying(100),
    stnum character varying(10),
    url character varying(40),
    ld date,
    h_school integer,
    m_school integer,
    "owner" character varying(20),
    ef character varying(100),
    broker character varying(40),
    vew bigint,
    lp integer,
    zip_code bigint,
    sqft character varying(8),
    ltyp character varying(4),
    stname character varying(20),
    e_school integer,
    agent_id character varying(6),
    status character varying(1),
    ar character varying(5),
    r_md timestamp without time zone
);


ALTER TABLE public.mul OWNER TO postgres;

--
-- Name: office; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE office (
    url character varying(40),
    name character varying(20),
    broker_id character varying(6),
    office_id character varying(7) NOT NULL,
    r_md timestamp without time zone
);


ALTER TABLE public.office OWNER TO postgres;

--
-- Name: rets_property_res; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rets_property_res (
    r_ld date,
    r_md timestamp without time zone,
    r_lp integer,
    r_baths integer,
    r_beds integer,
    r_agent_id character varying(12),
    r_city character varying(24),
    r_county character varying(24),
    r_ln character varying(40) NOT NULL,
    r_office_id character varying(12),
    r_zip_code character varying(10),
    r_state character varying(24),
    r_stnum character varying(12),
    r_street_dir_prefix character varying(12),
    r_stname character varying(24),
    r_ltyp character varying(4),
    r_board character varying(24),
    r_cldate date,
    r_codate date,
    r_cp integer,
    r_unnum character varying(12),
    r_ls character varying(12),
    r_garage integer,
    r_area integer,
    r_rooms integer,
    r_year_built integer,
    r_sqft double precision,
    r_thumb_md timestamp without time zone,
    r_thumbnails integer,
    r_photo_md timestamp without time zone,
    r_photos integer
);


ALTER TABLE public.rets_property_res OWNER TO postgres;

--
-- Name: xceligent; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE xceligent (
    property_id character(10),
    property_name character(80),
    address character(40),
    building_number character(20),
    city character(40),
    state character(40),
    county character(40),
    zip character(20),
    metro character(60),
    general_use character(20),
    gba character(20),
    gla character(20),
    nra character(20),
    building_size character(20),
    no_of_floors character(10),
    construction_status character(20),
    construction_class character(20),
    year_built character(10),
    listing_type character(30),
    sale_created_date character(20),
    sale_modified_date character(20),
    sale_listing_status character(20),
    sale_sharing_level character(10),
    sale_agent_id character(20),
    sale_agent_first_name character(60),
    sale_agent_last_name character(60),
    sale_email_address character(40),
    sale_company_name character(60),
    sale_agent_address character(80),
    sale_agent_city character(40),
    sale_agent_state character(20),
    sale_agent_zip character(20),
    specific_use character(60),
    tenancy_type character(40),
    sale_asking_price character(80),
    sale_agent_phone_number character(40),
    r_md timestamp without time zone
);


ALTER TABLE public.xceligent OWNER TO postgres;

--
-- Name: TABLE xceligent; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE xceligent IS 'xceligent sales listings';

--
-- Name: actagt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY actagt
    ADD CONSTRAINT actagt_pkey PRIMARY KEY (agent_id);


--
-- Name: agt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY agt
    ADD CONSTRAINT agt_pkey PRIMARY KEY (agent_id);


--
-- Name: con_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY con
    ADD CONSTRAINT con_pkey PRIMARY KEY (ln);


--
-- Name: lnd_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY lnd
    ADD CONSTRAINT lnd_pkey PRIMARY KEY (ln);


--
-- Name: mob_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mob
    ADD CONSTRAINT mob_pkey PRIMARY KEY (ln);


--
-- Name: mul_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mul
    ADD CONSTRAINT mul_pkey PRIMARY KEY (ln);


--
-- Name: office_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY office
    ADD CONSTRAINT office_pkey PRIMARY KEY (office_id);


--
-- Name: rets_property_res_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rets_property_res
    ADD CONSTRAINT rets_property_res_pkey PRIMARY KEY (r_ln);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: actagt; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE actagt FROM PUBLIC;
REVOKE ALL ON TABLE actagt FROM postgres;
GRANT ALL ON TABLE actagt TO postgres;


--
-- Name: agt; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE agt FROM PUBLIC;
REVOKE ALL ON TABLE agt FROM postgres;
GRANT ALL ON TABLE agt TO postgres;


--
-- Name: con; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE con FROM PUBLIC;
REVOKE ALL ON TABLE con FROM postgres;
GRANT ALL ON TABLE con TO postgres;


--
-- Name: lnd; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE lnd FROM PUBLIC;
REVOKE ALL ON TABLE lnd FROM postgres;
GRANT ALL ON TABLE lnd TO postgres;


--
-- Name: mob; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mob FROM PUBLIC;
REVOKE ALL ON TABLE mob FROM postgres;
GRANT ALL ON TABLE mob TO postgres;


--
-- Name: mul; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mul FROM PUBLIC;
REVOKE ALL ON TABLE mul FROM postgres;
GRANT ALL ON TABLE mul TO postgres;


--
-- Name: office; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE office FROM PUBLIC;
REVOKE ALL ON TABLE office FROM postgres;
GRANT ALL ON TABLE office TO postgres;

