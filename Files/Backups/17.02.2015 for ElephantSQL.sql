--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.5
-- Dumped by pg_dump version 9.3.5
-- Started on 2015-02-17 11:23:34

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 215 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2290 (class 0 OID 0)
-- Dependencies: 215
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 16395)
-- Name: address; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE address (
    address_id integer NOT NULL,
    address character varying(100) NOT NULL
);


ALTER TABLE public.address OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 16398)
-- Name: address_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.address_address_id_seq OWNER TO postgres;

--
-- TOC entry 2291 (class 0 OID 0)
-- Dependencies: 171
-- Name: address_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE address_address_id_seq OWNED BY address.address_id;


--
-- TOC entry 172 (class 1259 OID 16400)
-- Name: article; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE article (
    article_id integer NOT NULL,
    description character varying(1024) NOT NULL,
    packaging_unit character varying(100) NOT NULL,
    weightpu double precision NOT NULL,
    mdd date NOT NULL,
    pricepu numeric(15,2) NOT NULL
);


ALTER TABLE public.article OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 16406)
-- Name: article_article_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE article_article_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.article_article_id_seq OWNER TO postgres;

--
-- TOC entry 2292 (class 0 OID 0)
-- Dependencies: 173
-- Name: article_article_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE article_article_id_seq OWNED BY article.article_id;


--
-- TOC entry 174 (class 1259 OID 16408)
-- Name: incoming_article; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE incoming_article (
    incoming_article_id integer NOT NULL,
    incoming_delivery_id integer NOT NULL,
    article_id integer NOT NULL,
    article_nr integer,
    numberpu integer NOT NULL
);


ALTER TABLE public.incoming_article OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 16411)
-- Name: outgoing_article; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE outgoing_article (
    outgoing_article_id integer NOT NULL,
    outgoing_delivery_id integer NOT NULL,
    article_id integer NOT NULL,
    article_nr integer,
    numberpu integer NOT NULL
);


ALTER TABLE public.outgoing_article OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16857)
-- Name: totalnumberpuofallincomingarticles; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW totalnumberpuofallincomingarticles AS
 SELECT incoming_article.article_id,
    sum(incoming_article.numberpu) AS totalnumberofpus
   FROM incoming_article
  GROUP BY incoming_article.article_id;


ALTER TABLE public.totalnumberpuofallincomingarticles OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 16853)
-- Name: totalnumberpuofalloutgoingarticles; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW totalnumberpuofalloutgoingarticles AS
 SELECT outgoing_article.article_id,
    sum(outgoing_article.numberpu) AS totalnumberofpus
   FROM outgoing_article
  GROUP BY outgoing_article.article_id;


ALTER TABLE public.totalnumberpuofalloutgoingarticles OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 16873)
-- Name: availarticleindepot; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW availarticleindepot AS
 SELECT article_id,
    (COALESCE(inarticle.totalnumberofpus, (0)::bigint) - COALESCE(outarticle.totalnumberofpus, (0)::bigint)) AS availnumberofpus
   FROM (totalnumberpuofallincomingarticles inarticle
     FULL JOIN totalnumberpuofalloutgoingarticles outarticle USING (article_id));


ALTER TABLE public.availarticleindepot OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 16426)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE category (
    category_id integer NOT NULL,
    category character varying(100) NOT NULL,
    description character varying(1024)
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 16432)
-- Name: category_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE category_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.category_category_id_seq OWNER TO postgres;

--
-- TOC entry 2293 (class 0 OID 0)
-- Dependencies: 177
-- Name: category_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE category_category_id_seq OWNED BY category.category_id;


--
-- TOC entry 178 (class 1259 OID 16434)
-- Name: city; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE city (
    city_id integer NOT NULL,
    zip character varying(5) NOT NULL,
    city character varying(100) NOT NULL
);


ALTER TABLE public.city OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 16437)
-- Name: city_city_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE city_city_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.city_city_id_seq OWNER TO postgres;

--
-- TOC entry 2294 (class 0 OID 0)
-- Dependencies: 179
-- Name: city_city_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE city_city_id_seq OWNED BY city.city_id;


--
-- TOC entry 180 (class 1259 OID 16439)
-- Name: country; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE country (
    country_id integer NOT NULL,
    country character varying(50) NOT NULL
);


ALTER TABLE public.country OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 16442)
-- Name: country_country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE country_country_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.country_country_id_seq OWNER TO postgres;

--
-- TOC entry 2295 (class 0 OID 0)
-- Dependencies: 181
-- Name: country_country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE country_country_id_seq OWNED BY country.country_id;


--
-- TOC entry 182 (class 1259 OID 16444)
-- Name: delivery_list; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE delivery_list (
    delivery_list_id integer NOT NULL,
    person_id integer NOT NULL,
    name character varying(100) NOT NULL,
    date date NOT NULL,
    comment character varying(1000) NOT NULL,
    driver character varying(100) NOT NULL,
    passenger character varying(100) NOT NULL,
    update_timestamp date DEFAULT now() NOT NULL,
    archived integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.delivery_list OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 16452)
-- Name: delivery_list_delivery_list_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE delivery_list_delivery_list_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.delivery_list_delivery_list_id_seq OWNER TO postgres;

--
-- TOC entry 2296 (class 0 OID 0)
-- Dependencies: 183
-- Name: delivery_list_delivery_list_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE delivery_list_delivery_list_id_seq OWNED BY delivery_list.delivery_list_id;


--
-- TOC entry 184 (class 1259 OID 16454)
-- Name: email; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE email (
    email_id integer NOT NULL,
    person_id integer,
    type_id integer NOT NULL,
    email character varying(50) NOT NULL
);


ALTER TABLE public.email OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 16457)
-- Name: email_email_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE email_email_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.email_email_id_seq OWNER TO postgres;

--
-- TOC entry 2297 (class 0 OID 0)
-- Dependencies: 185
-- Name: email_email_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE email_email_id_seq OWNED BY email.email_id;


--
-- TOC entry 186 (class 1259 OID 16459)
-- Name: incoming_article_incoming_article_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE incoming_article_incoming_article_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.incoming_article_incoming_article_id_seq OWNER TO postgres;

--
-- TOC entry 2298 (class 0 OID 0)
-- Dependencies: 186
-- Name: incoming_article_incoming_article_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE incoming_article_incoming_article_id_seq OWNED BY incoming_article.incoming_article_id;


--
-- TOC entry 187 (class 1259 OID 16461)
-- Name: incoming_delivery; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE incoming_delivery (
    incoming_delivery_id integer NOT NULL,
    organisation_id integer NOT NULL,
    person_id integer NOT NULL,
    delivery_nr integer,
    date date NOT NULL,
    comment character varying(1000) NOT NULL,
    update_timestamp date DEFAULT now() NOT NULL,
    archived integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.incoming_delivery OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 16469)
-- Name: incoming_delivery_incoming_delivery_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE incoming_delivery_incoming_delivery_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.incoming_delivery_incoming_delivery_id_seq OWNER TO postgres;

--
-- TOC entry 2299 (class 0 OID 0)
-- Dependencies: 188
-- Name: incoming_delivery_incoming_delivery_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE incoming_delivery_incoming_delivery_id_seq OWNED BY incoming_delivery.incoming_delivery_id;


--
-- TOC entry 189 (class 1259 OID 16471)
-- Name: logging; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE logging (
    logging_id integer NOT NULL,
    loggingtext character varying(1000) NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    person_id integer NOT NULL
);


ALTER TABLE public.logging OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 16477)
-- Name: logging_logging_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE logging_logging_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.logging_logging_id_seq OWNER TO postgres;

--
-- TOC entry 2300 (class 0 OID 0)
-- Dependencies: 190
-- Name: logging_logging_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE logging_logging_id_seq OWNED BY logging.logging_id;


--
-- TOC entry 191 (class 1259 OID 16479)
-- Name: organisation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE organisation (
    organisation_id integer NOT NULL,
    address_id integer,
    city_id integer,
    country_id integer,
    person_id integer,
    name character varying(100) NOT NULL,
    comment character varying(1000) NOT NULL,
    update_timestamp date DEFAULT now() NOT NULL,
    active integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.organisation OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 16485)
-- Name: organisation_organisation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE organisation_organisation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.organisation_organisation_id_seq OWNER TO postgres;

--
-- TOC entry 2301 (class 0 OID 0)
-- Dependencies: 192
-- Name: organisation_organisation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE organisation_organisation_id_seq OWNED BY organisation.organisation_id;


--
-- TOC entry 193 (class 1259 OID 16487)
-- Name: orgcontactperson; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE orgcontactperson (
    person_id integer NOT NULL,
    organisation_id integer NOT NULL
);


ALTER TABLE public.orgcontactperson OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 16490)
-- Name: outgoing_article_outgoing_article_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE outgoing_article_outgoing_article_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.outgoing_article_outgoing_article_id_seq OWNER TO postgres;

--
-- TOC entry 2302 (class 0 OID 0)
-- Dependencies: 194
-- Name: outgoing_article_outgoing_article_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE outgoing_article_outgoing_article_id_seq OWNED BY outgoing_article.outgoing_article_id;


--
-- TOC entry 195 (class 1259 OID 16492)
-- Name: outgoing_delivery; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE outgoing_delivery (
    outgoing_delivery_id integer NOT NULL,
    organisation_id integer NOT NULL,
    person_id integer NOT NULL,
    delivery_list_id integer,
    delivery_nr integer,
    date date NOT NULL,
    comment character varying(1000) NOT NULL,
    update_timestamp date DEFAULT now() NOT NULL,
    archived integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.outgoing_delivery OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16500)
-- Name: outgoing_delivery_outgoing_delivery_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE outgoing_delivery_outgoing_delivery_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.outgoing_delivery_outgoing_delivery_id_seq OWNER TO postgres;

--
-- TOC entry 2303 (class 0 OID 0)
-- Dependencies: 196
-- Name: outgoing_delivery_outgoing_delivery_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE outgoing_delivery_outgoing_delivery_id_seq OWNED BY outgoing_delivery.outgoing_delivery_id;


--
-- TOC entry 197 (class 1259 OID 16502)
-- Name: permission; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE permission (
    permission_id integer NOT NULL,
    description character varying(1024),
    permission character varying(20) NOT NULL
);


ALTER TABLE public.permission OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16508)
-- Name: permission_permission_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE permission_permission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.permission_permission_id_seq OWNER TO postgres;

--
-- TOC entry 2304 (class 0 OID 0)
-- Dependencies: 198
-- Name: permission_permission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE permission_permission_id_seq OWNED BY permission.permission_id;


--
-- TOC entry 199 (class 1259 OID 16510)
-- Name: person; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE person (
    person_id integer NOT NULL,
    country_id integer,
    city_id integer,
    address_id integer,
    per_person_id integer,
    salutation character varying(20) NOT NULL,
    title character varying(20) NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    comment character varying(1000) NOT NULL,
    update_timestamp date DEFAULT now() NOT NULL,
    active integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.person OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16516)
-- Name: person_person_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_person_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.person_person_id_seq OWNER TO postgres;

--
-- TOC entry 2305 (class 0 OID 0)
-- Dependencies: 200
-- Name: person_person_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE person_person_id_seq OWNED BY person.person_id;


--
-- TOC entry 203 (class 1259 OID 16524)
-- Name: relorgtype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE relorgtype (
    type_id integer NOT NULL,
    organisation_id integer NOT NULL
);


ALTER TABLE public.relorgtype OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 16543)
-- Name: type; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE type (
    type_id integer NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.type OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 25019)
-- Name: personaddressreportview; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW personaddressreportview AS
 SELECT p.salutation,
    p.title,
    p.first_name AS "firstName",
    p.last_name AS "lastName",
    pa.address AS "privateAddress",
    pc.zip AS "privateZip",
    pc.city AS "privateCity",
    pcountry.country AS "privateCountry",
    o.name AS "orgName",
    ot.name AS "orgType",
    oa.address AS "orgAddress",
    oc.zip AS "orgZip",
    oc.city AS "orgCity",
    ocountry.country AS "orgCountry"
   FROM ((((((((((person p
     LEFT JOIN orgcontactperson USING (person_id))
     LEFT JOIN organisation o USING (organisation_id))
     LEFT JOIN relorgtype ON ((relorgtype.organisation_id = o.organisation_id)))
     LEFT JOIN type ot ON ((ot.type_id = relorgtype.type_id)))
     LEFT JOIN address pa ON ((p.address_id = pa.address_id)))
     LEFT JOIN address oa ON ((o.address_id = oa.address_id)))
     LEFT JOIN city pc ON ((p.city_id = pc.city_id)))
     LEFT JOIN city oc ON ((o.city_id = oc.city_id)))
     LEFT JOIN country pcountry ON ((p.country_id = pcountry.country_id)))
     LEFT JOIN country ocountry ON ((o.country_id = ocountry.country_id)))
  ORDER BY p.last_name;


ALTER TABLE public.personaddressreportview OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16518)
-- Name: platformuser; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE platformuser (
    person_id integer NOT NULL,
    permission_id integer NOT NULL,
    password character varying(200) NOT NULL,
    login_email character varying(50) NOT NULL
);


ALTER TABLE public.platformuser OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16521)
-- Name: relorgcat; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE relorgcat (
    organisation_id integer NOT NULL,
    category_id integer NOT NULL
);


ALTER TABLE public.relorgcat OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16527)
-- Name: relpersontype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE relpersontype (
    type_id integer NOT NULL,
    person_id integer NOT NULL
);


ALTER TABLE public.relpersontype OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16530)
-- Name: reporting; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE reporting (
    report_id integer NOT NULL,
    date date NOT NULL,
    description character varying(1024),
    report_file character(1)
);


ALTER TABLE public.reporting OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16536)
-- Name: reporting_report_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE reporting_report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reporting_report_id_seq OWNER TO postgres;

--
-- TOC entry 2306 (class 0 OID 0)
-- Dependencies: 206
-- Name: reporting_report_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE reporting_report_id_seq OWNED BY reporting.report_id;


--
-- TOC entry 207 (class 1259 OID 16538)
-- Name: telephone; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE telephone (
    telephone_id integer NOT NULL,
    person_id integer,
    type_id integer NOT NULL,
    telephone character varying(20) NOT NULL
);


ALTER TABLE public.telephone OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16541)
-- Name: telephone_telephone_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE telephone_telephone_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.telephone_telephone_id_seq OWNER TO postgres;

--
-- TOC entry 2307 (class 0 OID 0)
-- Dependencies: 208
-- Name: telephone_telephone_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE telephone_telephone_id_seq OWNED BY telephone.telephone_id;


--
-- TOC entry 210 (class 1259 OID 16546)
-- Name: type_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE type_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.type_type_id_seq OWNER TO postgres;

--
-- TOC entry 2308 (class 0 OID 0)
-- Dependencies: 210
-- Name: type_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE type_type_id_seq OWNED BY type.type_id;


--
-- TOC entry 1971 (class 2604 OID 16548)
-- Name: address_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address ALTER COLUMN address_id SET DEFAULT nextval('address_address_id_seq'::regclass);


--
-- TOC entry 1972 (class 2604 OID 16549)
-- Name: article_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY article ALTER COLUMN article_id SET DEFAULT nextval('article_article_id_seq'::regclass);


--
-- TOC entry 1975 (class 2604 OID 16550)
-- Name: category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY category ALTER COLUMN category_id SET DEFAULT nextval('category_category_id_seq'::regclass);


--
-- TOC entry 1976 (class 2604 OID 16551)
-- Name: city_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY city ALTER COLUMN city_id SET DEFAULT nextval('city_city_id_seq'::regclass);


--
-- TOC entry 1977 (class 2604 OID 16552)
-- Name: country_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY country ALTER COLUMN country_id SET DEFAULT nextval('country_country_id_seq'::regclass);


--
-- TOC entry 1980 (class 2604 OID 16553)
-- Name: delivery_list_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY delivery_list ALTER COLUMN delivery_list_id SET DEFAULT nextval('delivery_list_delivery_list_id_seq'::regclass);


--
-- TOC entry 1981 (class 2604 OID 16554)
-- Name: email_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY email ALTER COLUMN email_id SET DEFAULT nextval('email_email_id_seq'::regclass);


--
-- TOC entry 1973 (class 2604 OID 16555)
-- Name: incoming_article_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY incoming_article ALTER COLUMN incoming_article_id SET DEFAULT nextval('incoming_article_incoming_article_id_seq'::regclass);


--
-- TOC entry 1984 (class 2604 OID 16556)
-- Name: incoming_delivery_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY incoming_delivery ALTER COLUMN incoming_delivery_id SET DEFAULT nextval('incoming_delivery_incoming_delivery_id_seq'::regclass);


--
-- TOC entry 1985 (class 2604 OID 16557)
-- Name: logging_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY logging ALTER COLUMN logging_id SET DEFAULT nextval('logging_logging_id_seq'::regclass);


--
-- TOC entry 1986 (class 2604 OID 16558)
-- Name: organisation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY organisation ALTER COLUMN organisation_id SET DEFAULT nextval('organisation_organisation_id_seq'::regclass);


--
-- TOC entry 1974 (class 2604 OID 16559)
-- Name: outgoing_article_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outgoing_article ALTER COLUMN outgoing_article_id SET DEFAULT nextval('outgoing_article_outgoing_article_id_seq'::regclass);


--
-- TOC entry 1991 (class 2604 OID 16560)
-- Name: outgoing_delivery_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outgoing_delivery ALTER COLUMN outgoing_delivery_id SET DEFAULT nextval('outgoing_delivery_outgoing_delivery_id_seq'::regclass);


--
-- TOC entry 1992 (class 2604 OID 16561)
-- Name: permission_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permission ALTER COLUMN permission_id SET DEFAULT nextval('permission_permission_id_seq'::regclass);


--
-- TOC entry 1993 (class 2604 OID 16562)
-- Name: person_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person ALTER COLUMN person_id SET DEFAULT nextval('person_person_id_seq'::regclass);


--
-- TOC entry 1996 (class 2604 OID 16563)
-- Name: report_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY reporting ALTER COLUMN report_id SET DEFAULT nextval('reporting_report_id_seq'::regclass);


--
-- TOC entry 1997 (class 2604 OID 16564)
-- Name: telephone_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telephone ALTER COLUMN telephone_id SET DEFAULT nextval('telephone_telephone_id_seq'::regclass);


--
-- TOC entry 1998 (class 2604 OID 16565)
-- Name: type_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY type ALTER COLUMN type_id SET DEFAULT nextval('type_type_id_seq'::regclass);


--
-- TOC entry 2242 (class 0 OID 16395)
-- Dependencies: 170
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO address VALUES (2, 'Alte Landstraße 10A');
INSERT INTO address VALUES (3, 'neue Straße');
INSERT INTO address VALUES (4, 'Alte Landstraße 10B');
INSERT INTO address VALUES (5, 'keine Ahnung');
INSERT INTO address VALUES (6, 'Bauernhof 1');
INSERT INTO address VALUES (7, 'Bauernhof 2');
INSERT INTO address VALUES (8, 'Buch 1');
INSERT INTO address VALUES (9, 'Busch 1');
INSERT INTO address VALUES (10, 'Lehmhütte 4B');
INSERT INTO address VALUES (11, 'lskdjflks');
INSERT INTO address VALUES (13, 'Herberts Adresse');
INSERT INTO address VALUES (15, 'McLovin Street');
INSERT INTO address VALUES (16, 'Straße 10');
INSERT INTO address VALUES (17, 'Fürstenbrunnerstraße 8');
INSERT INTO address VALUES (18, 'Antheringer Hauptstraße 8');
INSERT INTO address VALUES (19, 'Gewerbestraße 7');
INSERT INTO address VALUES (20, 'Klosterstraße 1');
INSERT INTO address VALUES (21, 'Ignaz-Harrer-Straße 83a');
INSERT INTO address VALUES (22, 'Alte Straße');
INSERT INTO address VALUES (23, 'Ziegeleistraße X');
INSERT INTO address VALUES (24, 'Wienerstraße');
INSERT INTO address VALUES (25, 'Julius-Fritsche Gasse');
INSERT INTO address VALUES (26, 'Herrmann-Gmeiner-Straße 29');
INSERT INTO address VALUES (27, 'Marktplatz 8');
INSERT INTO address VALUES (28, 'Straße 1');


--
-- TOC entry 2309 (class 0 OID 0)
-- Dependencies: 171
-- Name: address_address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('address_address_id_seq', 28, true);


--
-- TOC entry 2244 (class 0 OID 16400)
-- Dependencies: 172
-- Data for Name: article; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO article VALUES (137, 'Topfen', 'Becher', 0.25, '2015-02-20', 0.45);
INSERT INTO article VALUES (138, 'Joghurt', 'Becher', 0.25, '2015-02-20', 0.35);
INSERT INTO article VALUES (139, 'Schokolade', 'Tafel', 0.25, '2015-05-12', 0.75);
INSERT INTO article VALUES (140, 'brot', 'pkg', 1, '2016-08-01', 1.00);


--
-- TOC entry 2310 (class 0 OID 0)
-- Dependencies: 173
-- Name: article_article_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('article_article_id_seq', 140, true);


--
-- TOC entry 2248 (class 0 OID 16426)
-- Dependencies: 176
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO category VALUES (10, 'Umgebung Salzburg', 'Lieferant im der Umgebung von Salzburg Stadt');
INSERT INTO category VALUES (9, 'Allgemeiner Lieferant', 'Liefert alle Arten von Lebensmitteln');
INSERT INTO category VALUES (13, 'Pasta', 'mögen gerne Pasta');
INSERT INTO category VALUES (14, 'Gemüse', 'mögen gerne Gemüse');
INSERT INTO category VALUES (15, 'Brot', 'viel Brot');
INSERT INTO category VALUES (16, 'Lagerraum groß', 'haben einen großen Lagerraum (>10m²)');
INSERT INTO category VALUES (17, 'Stadt Salzburg', 'befindet sich in der Stadt Salzburg');


--
-- TOC entry 2311 (class 0 OID 0)
-- Dependencies: 177
-- Name: category_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('category_category_id_seq', 17, true);


--
-- TOC entry 2250 (class 0 OID 16434)
-- Dependencies: 178
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO city VALUES (6, '5112', 'Lamprechtshausen');
INSERT INTO city VALUES (7, '5110', 'Oberndorf');
INSERT INTO city VALUES (8, '5112', 'Oberndorf');
INSERT INTO city VALUES (9, '1234', 'Fürstenbrunn');
INSERT INTO city VALUES (10, '5110', 'Fürstenbrunn');
INSERT INTO city VALUES (11, '5112', 'Arnsdorf');
INSERT INTO city VALUES (12, '5082', 'Fürstenbrunn');
INSERT INTO city VALUES (13, '8943', 'Braundorf');
INSERT INTO city VALUES (14, '8943', 'Dorf ohne Wasser');
INSERT INTO city VALUES (15, 'kA', 'GutDorf');
INSERT INTO city VALUES (16, '12345', 'ljslkdf');
INSERT INTO city VALUES (18, '5020', 'Salzburg');
INSERT INTO city VALUES (20, 'zip', 'McLovin City');
INSERT INTO city VALUES (21, '5102', 'Anthering');
INSERT INTO city VALUES (22, '4020', 'Linz');
INSERT INTO city VALUES (23, '5111', 'Bürmoos');
INSERT INTO city VALUES (29, '5201', 'Seekirchen');
INSERT INTO city VALUES (30, '5204', 'Straßwalchen');
INSERT INTO city VALUES (31, '', 'Salzburg');
INSERT INTO city VALUES (32, '5020', '');


--
-- TOC entry 2312 (class 0 OID 0)
-- Dependencies: 179
-- Name: city_city_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('city_city_id_seq', 32, true);


--
-- TOC entry 2252 (class 0 OID 16439)
-- Dependencies: 180
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO country VALUES (5, 'Österreich');
INSERT INTO country VALUES (6, 'Deutschland');
INSERT INTO country VALUES (7, 'Irland');
INSERT INTO country VALUES (8, 'Nigeria');
INSERT INTO country VALUES (9, 'Estareich');
INSERT INTO country VALUES (10, 'lakjdsflkjas');


--
-- TOC entry 2313 (class 0 OID 0)
-- Dependencies: 181
-- Name: country_country_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('country_country_id_seq', 10, true);


--
-- TOC entry 2254 (class 0 OID 16444)
-- Dependencies: 182
-- Data for Name: delivery_list; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO delivery_list VALUES (93, 36, '', '2015-02-10', 'grias di', 'seppi', 'franzi', '2015-02-13', 0);
INSERT INTO delivery_list VALUES (94, 36, '', '2015-02-20', 'supper fahrt', 'Franzi', 'Martin', '2015-02-17', 0);


--
-- TOC entry 2314 (class 0 OID 0)
-- Dependencies: 183
-- Name: delivery_list_delivery_list_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('delivery_list_delivery_list_id_seq', 94, true);


--
-- TOC entry 2256 (class 0 OID 16454)
-- Dependencies: 184
-- Data for Name: email; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO email VALUES (16, 36, 11, 'mschnoell@Privat.at');
INSERT INTO email VALUES (29, 37, 10, 's.stadlmair@gmx.at');
INSERT INTO email VALUES (30, 47, 10, 'max.mustermann@gmail.com');
INSERT INTO email VALUES (31, NULL, 11, 'mail@mail.com');


--
-- TOC entry 2315 (class 0 OID 0)
-- Dependencies: 185
-- Name: email_email_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('email_email_id_seq', 31, true);


--
-- TOC entry 2246 (class 0 OID 16408)
-- Dependencies: 174
-- Data for Name: incoming_article; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO incoming_article VALUES (125, 59, 137, 1, 200);
INSERT INTO incoming_article VALUES (126, 59, 138, 0, 500);
INSERT INTO incoming_article VALUES (127, 60, 139, 0, 200);
INSERT INTO incoming_article VALUES (128, 61, 140, 0, 20);


--
-- TOC entry 2316 (class 0 OID 0)
-- Dependencies: 186
-- Name: incoming_article_incoming_article_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('incoming_article_incoming_article_id_seq', 128, true);


--
-- TOC entry 2259 (class 0 OID 16461)
-- Dependencies: 187
-- Data for Name: incoming_delivery; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO incoming_delivery VALUES (59, 10, 36, 0, '2015-02-10', 'Super große Lieferung', '2015-02-10', 0);
INSERT INTO incoming_delivery VALUES (60, 8, 36, 0, '2015-02-13', 'nette Lieferung', '2015-02-12', 0);
INSERT INTO incoming_delivery VALUES (61, 11, 36, 0, '2015-02-01', 'hallo seas', '2015-02-13', 0);


--
-- TOC entry 2317 (class 0 OID 0)
-- Dependencies: 188
-- Name: incoming_delivery_incoming_delivery_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('incoming_delivery_incoming_delivery_id_seq', 61, true);


--
-- TOC entry 2261 (class 0 OID 16471)
-- Dependencies: 189
-- Data for Name: logging; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO logging VALUES (17, '[INFO] Warenausgang mit der id 58 gespeichert', '2015-02-06 13:27:58.909', 36);
INSERT INTO logging VALUES (18, '[INFO] Lieferliste mit der id 88 gespeichert', '2015-02-06 13:28:11.436', 36);
INSERT INTO logging VALUES (19, '[INFO] Lieferliste mit der id 88 auf Archivierungsstatus 1 gesetzt', '2015-02-06 13:28:18.315', 36);
INSERT INTO logging VALUES (20, '[INFO] Lieferliste mit der id 88 auf Archivierungsstatus 0 gesetzt', '2015-02-06 13:28:26.224', 36);
INSERT INTO logging VALUES (21, '[INFO] Warenausgang mit der id 53 auf Archivierungsstatus 1 gesetzt', '2015-02-06 13:28:33.712', 36);
INSERT INTO logging VALUES (22, '[INFO] Warenausgang mit der id 53 auf Archivierungsstatus 0 gesetzt', '2015-02-06 13:28:44.367', 36);
INSERT INTO logging VALUES (23, '[INFO] Warenausgang mit der id 60 auf Archivierungsstatus 0 gesetzt', '2015-02-06 13:28:48.47', 36);
INSERT INTO logging VALUES (24, '[INFO] Wareneingang mit der id 55 auf Archivierungsstatus 1 gesetzt', '2015-02-06 13:28:56.005', 36);
INSERT INTO logging VALUES (25, '[INFO] Wareneingang mit der id 55 auf Archivierungsstatus 0 gesetzt', '2015-02-06 13:29:03.399', 36);
INSERT INTO logging VALUES (26, '[INFO] Wareneingang mit der id 54 auf Archivierungsstatus 0 gesetzt', '2015-02-06 13:29:15.567', 36);
INSERT INTO logging VALUES (27, '[INFO] Wareneingang mit der id 54 gelöscht', '2015-02-06 13:29:22.619', 36);
INSERT INTO logging VALUES (28, '[INFO] Wareneingang mit der id 56 gelöscht', '2015-02-06 13:29:29.373', 36);
INSERT INTO logging VALUES (29, '[INFO] Wareneingang mit der id 51 gelöscht', '2015-02-06 13:29:32.509', 36);
INSERT INTO logging VALUES (30, '[INFO] Warenausgang mit der id 60 gelöscht', '2015-02-06 13:29:48.998', 36);
INSERT INTO logging VALUES (31, '[INFO] Warenausgang mit der id 58 gelöscht', '2015-02-06 13:29:51.697', 36);
INSERT INTO logging VALUES (32, '[INFO] Lieferliste mit der id 88 gelöscht', '2015-02-06 13:31:07.154', 36);
INSERT INTO logging VALUES (33, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-06 13:38:21.313', 36);
INSERT INTO logging VALUES (34, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-06 15:02:20.469', 36);
INSERT INTO logging VALUES (35, '[INFO] Warenausgang mit der id 61 gespeichert', '2015-02-06 15:03:07.035', 36);
INSERT INTO logging VALUES (36, '[INFO] Warenausgang mit der id 61 gespeichert', '2015-02-06 15:03:24.866', 36);
INSERT INTO logging VALUES (37, '[INFO] Lieferliste mit der id 91 gespeichert', '2015-02-06 15:03:49.904', 36);
INSERT INTO logging VALUES (38, '[INFO] Wareneingang mit der id 57 gespeichert', '2015-02-06 15:06:59.991', 36);
INSERT INTO logging VALUES (39, '[INFO] Warenausgang mit der id 62 gespeichert', '2015-02-06 15:07:49.74', 36);
INSERT INTO logging VALUES (40, '[INFO] Wareneingang mit der id 58 gespeichert', '2015-02-06 15:09:25.368', 36);
INSERT INTO logging VALUES (41, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 09:01:29.323', 36);
INSERT INTO logging VALUES (42, '[INFO] Person mit der id 51 gelöscht', '2015-02-09 09:02:13.083', 36);
INSERT INTO logging VALUES (43, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:07:45.388', 36);
INSERT INTO logging VALUES (44, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:10:49.858', 36);
INSERT INTO logging VALUES (45, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:12:07.726', 36);
INSERT INTO logging VALUES (46, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:13:32.55', 36);
INSERT INTO logging VALUES (47, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:15:37.463', 36);
INSERT INTO logging VALUES (48, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:18:01.221', 36);
INSERT INTO logging VALUES (49, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:21:27.092', 36);
INSERT INTO logging VALUES (50, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:22:34.892', 36);
INSERT INTO logging VALUES (51, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:30:45.397', 36);
INSERT INTO logging VALUES (52, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 11:31:48.44', 36);
INSERT INTO logging VALUES (53, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 13:37:30.531', 36);
INSERT INTO logging VALUES (54, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 13:39:01.844', 36);
INSERT INTO logging VALUES (55, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-09 15:46:59.316', 36);
INSERT INTO logging VALUES (56, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 10:58:26.468', 36);
INSERT INTO logging VALUES (57, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 11:38:43.813', 36);
INSERT INTO logging VALUES (58, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 11:39:56.316', 36);
INSERT INTO logging VALUES (59, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:01:38.12', 36);
INSERT INTO logging VALUES (60, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:06:38.268', 36);
INSERT INTO logging VALUES (61, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:09:42.924', 36);
INSERT INTO logging VALUES (62, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:11:15.208', 36);
INSERT INTO logging VALUES (63, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:11:50.522', 36);
INSERT INTO logging VALUES (64, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:13:07.111', 36);
INSERT INTO logging VALUES (65, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:20:24.874', 36);
INSERT INTO logging VALUES (66, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:22:27.417', 36);
INSERT INTO logging VALUES (67, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 12:25:18.848', 36);
INSERT INTO logging VALUES (68, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 13:02:18.548', 36);
INSERT INTO logging VALUES (69, '[INFO] Warenausgang mit der id 62 gespeichert', '2015-02-10 14:11:51.838', 36);
INSERT INTO logging VALUES (70, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 15:55:44.392', 36);
INSERT INTO logging VALUES (71, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 16:13:53.761', 36);
INSERT INTO logging VALUES (72, '[INFO] Wareneingang mit der id 57 gelöscht', '2015-02-10 16:21:51.114', 36);
INSERT INTO logging VALUES (73, '[INFO] Wareneingang mit der id 58 gelöscht', '2015-02-10 16:22:02.464', 36);
INSERT INTO logging VALUES (74, '[INFO] Wareneingang mit der id 52 auf Archivierungsstatus 0 gesetzt', '2015-02-10 16:22:05.704', 36);
INSERT INTO logging VALUES (75, '[INFO] Wareneingang mit der id 47 auf Archivierungsstatus 0 gesetzt', '2015-02-10 16:22:07.854', 36);
INSERT INTO logging VALUES (76, '[INFO] Wareneingang mit der id 49 auf Archivierungsstatus 0 gesetzt', '2015-02-10 16:22:09.494', 36);
INSERT INTO logging VALUES (77, '[INFO] Lieferliste mit der id 91 gelöscht', '2015-02-10 16:22:21.214', 36);
INSERT INTO logging VALUES (78, '[INFO] Warenausgang mit der id 62 gespeichert', '2015-02-10 16:22:40.114', 36);
INSERT INTO logging VALUES (79, '[INFO] Warenausgang mit der id 63 gespeichert', '2015-02-10 16:23:07.214', 36);
INSERT INTO logging VALUES (80, '[INFO] Warenausgang mit der id 64 gespeichert', '2015-02-10 16:23:25.354', 36);
INSERT INTO logging VALUES (81, '[INFO] Warenausgang mit der id 62 gespeichert', '2015-02-10 16:25:19.966', 36);
INSERT INTO logging VALUES (82, '[INFO] Warenausgang mit der id 62 gelöscht', '2015-02-10 16:25:50.226', 36);
INSERT INTO logging VALUES (84, '[INFO] Warenausgang mit der id 63 gelöscht', '2015-02-10 16:27:29.806', 36);
INSERT INTO logging VALUES (85, '[INFO] Warenausgang mit der id 61 gelöscht', '2015-02-10 16:27:36.676', 36);
INSERT INTO logging VALUES (86, '[INFO] Wareneingang mit der id 52 gelöscht', '2015-02-10 16:27:43.526', 36);
INSERT INTO logging VALUES (87, '[INFO] Warenausgang mit der id 53 gelöscht', '2015-02-10 16:28:17.616', 36);
INSERT INTO logging VALUES (88, '[INFO] Wareneingang mit der id 55 gelöscht', '2015-02-10 16:28:21.506', 36);
INSERT INTO logging VALUES (83, '[INFO] Warenausgang mit der id 64 gelöscht', '2015-02-10 16:27:27.336', 36);
INSERT INTO logging VALUES (89, '[INFO] Wareneingang mit der id 47 gelöscht', '2015-02-10 16:28:25.196', 36);
INSERT INTO logging VALUES (90, '[INFO] Wareneingang mit der id 49 gelöscht', '2015-02-10 16:28:28.056', 36);
INSERT INTO logging VALUES (91, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 16:36:07.489', 36);
INSERT INTO logging VALUES (92, '[INFO] Wareneingang mit der id 59 gespeichert', '2015-02-10 16:38:10.449', 36);
INSERT INTO logging VALUES (93, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-10 16:55:02.351', 36);
INSERT INTO logging VALUES (94, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 08:06:00.545', 36);
INSERT INTO logging VALUES (95, '[INFO] Warenausgang mit der id 65 gespeichert', '2015-02-11 08:13:57.643', 36);
INSERT INTO logging VALUES (96, '[INFO] Warenausgang mit der id 65 gelöscht', '2015-02-11 08:14:57.726', 36);
INSERT INTO logging VALUES (97, '[INFO] Kategorie mit der id 13 gespeichert', '2015-02-11 08:17:29.706', 36);
INSERT INTO logging VALUES (98, '[INFO] Kategorie mit der id 14 gespeichert', '2015-02-11 08:17:40.716', 36);
INSERT INTO logging VALUES (99, '[INFO] Organisation mit der id 13 gespeichert', '2015-02-11 08:18:27.288', 36);
INSERT INTO logging VALUES (100, '[INFO] Kategorie mit der id 15 gespeichert', '2015-02-11 08:18:45.658', 36);
INSERT INTO logging VALUES (101, '[INFO] Kategorie mit der id 16 gespeichert', '2015-02-11 08:19:09.358', 36);
INSERT INTO logging VALUES (102, '[INFO] Organisation mit der id 13 gespeichert', '2015-02-11 08:19:20.788', 36);
INSERT INTO logging VALUES (103, '[INFO] Organisation mit der id 21 gespeichert', '2015-02-11 08:19:33.53', 36);
INSERT INTO logging VALUES (104, '[INFO] Organisation mit der id 12 gespeichert', '2015-02-11 08:19:42.45', 36);
INSERT INTO logging VALUES (105, '[INFO] Organisation mit der id 19 gespeichert', '2015-02-11 08:19:53.22', 36);
INSERT INTO logging VALUES (106, '[INFO] Organisation mit der id 9 gespeichert', '2015-02-11 08:20:00.53', 36);
INSERT INTO logging VALUES (107, '[INFO] Kategorie mit der id 17 gespeichert', '2015-02-11 08:20:16.93', 36);
INSERT INTO logging VALUES (108, '[INFO] Organisation mit der id 9 gespeichert', '2015-02-11 08:20:25.37', 36);
INSERT INTO logging VALUES (109, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 09:11:15.162', 36);
INSERT INTO logging VALUES (110, '[INFO] Warenausgang mit der id 66 gespeichert', '2015-02-11 09:11:33.184', 36);
INSERT INTO logging VALUES (111, '[INFO] Organisation mit der id 13 gespeichert', '2015-02-11 11:14:13.108', 36);
INSERT INTO logging VALUES (112, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 12:42:27.696', 36);
INSERT INTO logging VALUES (113, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 12:44:06.06', 36);
INSERT INTO logging VALUES (114, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 13:21:24.991', 36);
INSERT INTO logging VALUES (115, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 13:23:32.983', 36);
INSERT INTO logging VALUES (116, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 13:25:59.975', 36);
INSERT INTO logging VALUES (117, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 13:27:20.315', 36);
INSERT INTO logging VALUES (118, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 13:47:36.57', 36);
INSERT INTO logging VALUES (119, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 13:57:06.233', 36);
INSERT INTO logging VALUES (120, '[INFO] Lieferliste mit der id 92 gespeichert', '2015-02-11 13:57:33.827', 36);
INSERT INTO logging VALUES (121, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 14:13:01.349', 36);
INSERT INTO logging VALUES (122, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 14:15:21.715', 36);
INSERT INTO logging VALUES (123, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 14:20:56.522', 36);
INSERT INTO logging VALUES (124, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-11 15:02:49.938', 36);
INSERT INTO logging VALUES (125, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 10:53:28.772', 36);
INSERT INTO logging VALUES (126, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:22:59.035', 36);
INSERT INTO logging VALUES (127, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:29:47.165', 36);
INSERT INTO logging VALUES (128, '[INFO] Warenausgang mit der id 67 gespeichert', '2015-02-12 11:32:36.41', 36);
INSERT INTO logging VALUES (129, '[INFO] Warenausgang mit der id 68 gespeichert', '2015-02-12 11:33:17.329', 36);
INSERT INTO logging VALUES (130, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:34:41.866', 36);
INSERT INTO logging VALUES (131, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:37:42.587', 36);
INSERT INTO logging VALUES (132, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:40:08.183', 36);
INSERT INTO logging VALUES (133, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:42:27.101', 36);
INSERT INTO logging VALUES (134, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:44:04.32', 36);
INSERT INTO logging VALUES (135, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:45:52.335', 36);
INSERT INTO logging VALUES (136, '[INFO] Warenausgang mit der id 67 gespeichert', '2015-02-12 11:48:09.303', 36);
INSERT INTO logging VALUES (137, '[INFO] Warenausgang mit der id 68 gespeichert', '2015-02-12 11:48:17.821', 36);
INSERT INTO logging VALUES (138, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:50:49.863', 36);
INSERT INTO logging VALUES (139, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:53:01.855', 36);
INSERT INTO logging VALUES (140, '[INFO] Wareneingang mit der id 60 gespeichert', '2015-02-12 11:54:44.16', 36);
INSERT INTO logging VALUES (141, '[INFO] Warenausgang mit der id 68 gespeichert', '2015-02-12 11:55:20.883', 36);
INSERT INTO logging VALUES (142, '[INFO] Warenausgang mit der id 67 gespeichert', '2015-02-12 11:55:40.118', 36);
INSERT INTO logging VALUES (143, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 11:59:56.349', 36);
INSERT INTO logging VALUES (144, '[INFO] Organisation mit der id 22 gespeichert', '2015-02-12 12:00:32.962', 36);
INSERT INTO logging VALUES (145, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 12:04:40.395', 36);
INSERT INTO logging VALUES (146, '[INFO] Organisation mit der id 22 gespeichert', '2015-02-12 12:05:12.562', 36);
INSERT INTO logging VALUES (147, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 12:08:00.278', 36);
INSERT INTO logging VALUES (148, '[INFO] Organisation mit der id 23 gespeichert', '2015-02-12 12:10:47.87', 36);
INSERT INTO logging VALUES (149, '[INFO] Warenausgang mit der id 69 gespeichert', '2015-02-12 12:11:35.747', 36);
INSERT INTO logging VALUES (150, '[INFO] Organisation mit der id 23 gespeichert', '2015-02-12 12:12:12.127', 36);
INSERT INTO logging VALUES (151, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 13:27:31.038', 36);
INSERT INTO logging VALUES (152, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 13:57:46.082', 36);
INSERT INTO logging VALUES (153, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 13:59:47.778', 36);
INSERT INTO logging VALUES (154, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-12 15:37:20.311', 36);
INSERT INTO logging VALUES (155, '[INFO] Warenausgang mit der id 69 gelöscht', '2015-02-12 15:42:34.87', 36);
INSERT INTO logging VALUES (156, '[INFO] Warenausgang mit der id 68 gelöscht', '2015-02-12 15:42:41.16', 36);
INSERT INTO logging VALUES (157, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-13 11:22:38.765', 36);
INSERT INTO logging VALUES (158, '[INFO] Lieferliste mit der id 92 gelöscht', '2015-02-13 12:16:20.765', 36);
INSERT INTO logging VALUES (159, '[INFO] Warenausgang mit der id 70 gespeichert', '2015-02-13 12:25:38.336', 36);
INSERT INTO logging VALUES (160, '[INFO] Lieferliste mit der id 93 gespeichert', '2015-02-13 12:28:44.141', 36);
INSERT INTO logging VALUES (161, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-13 12:39:49.607', 36);
INSERT INTO logging VALUES (162, '[INFO] Wareneingang mit der id 61 gespeichert', '2015-02-13 12:41:19.036', 36);
INSERT INTO logging VALUES (163, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 09:22:19.745', 36);
INSERT INTO logging VALUES (164, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 10:38:33.56', 36);
INSERT INTO logging VALUES (165, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 11:17:15.827', 36);
INSERT INTO logging VALUES (166, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 14:54:19.964', 36);
INSERT INTO logging VALUES (167, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 15:18:25.432', 36);
INSERT INTO logging VALUES (168, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 15:24:16.897', 36);
INSERT INTO logging VALUES (169, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 15:38:16.113', 36);
INSERT INTO logging VALUES (170, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 15:51:40.141', 36);
INSERT INTO logging VALUES (171, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 15:53:51.951', 36);
INSERT INTO logging VALUES (172, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 15:55:59.043', 36);
INSERT INTO logging VALUES (173, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 16:02:33.508', 36);
INSERT INTO logging VALUES (174, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 16:05:05.697', 36);
INSERT INTO logging VALUES (175, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 16:10:29.961', 36);
INSERT INTO logging VALUES (176, '[INFO] Artikelverteilung für Artikel mit der Id 140 geändert', '2015-02-16 16:10:34.962', 36);
INSERT INTO logging VALUES (177, '[INFO] Person mit der id 48 gelöscht', '2015-02-16 16:12:24.391', 36);
INSERT INTO logging VALUES (178, '[INFO] Artikelverteilung für Artikel mit der Id 140 geändert', '2015-02-16 16:13:52.576', 36);
INSERT INTO logging VALUES (179, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 16:54:06.458', 36);
INSERT INTO logging VALUES (180, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 17:16:50.363', 36);
INSERT INTO logging VALUES (181, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 17:18:25.082', 36);
INSERT INTO logging VALUES (182, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 17:23:59.591', 36);
INSERT INTO logging VALUES (183, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 17:26:18.17', 36);
INSERT INTO logging VALUES (184, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 17:29:19.25', 36);
INSERT INTO logging VALUES (185, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-16 17:30:58.552', 36);
INSERT INTO logging VALUES (186, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 08:17:34.367', 36);
INSERT INTO logging VALUES (187, '[INFO] Warenausgang mit der id 71 gespeichert', '2015-02-17 08:17:54.901', 36);
INSERT INTO logging VALUES (188, '[INFO] Lieferliste mit der id 94 gespeichert', '2015-02-17 09:35:22.273', 36);
INSERT INTO logging VALUES (189, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 10:16:46.323', 36);
INSERT INTO logging VALUES (190, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 10:28:14.579', 36);
INSERT INTO logging VALUES (191, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 10:42:37.357', 36);
INSERT INTO logging VALUES (192, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 10:46:58.72', 36);
INSERT INTO logging VALUES (193, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 10:48:21.54', 36);
INSERT INTO logging VALUES (194, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 11:00:53.978', 36);
INSERT INTO logging VALUES (195, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 11:04:23.05', 36);
INSERT INTO logging VALUES (196, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 11:06:11.049', 36);
INSERT INTO logging VALUES (197, '[INFO] mschnoell@gmx.net hat sich angemeldet', '2015-02-17 11:12:24.997', 36);
INSERT INTO logging VALUES (198, '[INFO] Organisation mit der id 8 gelöscht', '2015-02-17 11:17:44.907', 36);


--
-- TOC entry 2318 (class 0 OID 0)
-- Dependencies: 190
-- Name: logging_logging_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('logging_logging_id_seq', 198, true);


--
-- TOC entry 2263 (class 0 OID 16479)
-- Dependencies: 191
-- Data for Name: organisation; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO organisation VALUES (11, 23, 18, 5, 36, 'Salzburg Patisserie', '', '2015-02-02', 1);
INSERT INTO organisation VALUES (10, 22, 7, 5, 36, 'Eurospar', '', '2015-02-06', 1);
INSERT INTO organisation VALUES (21, 27, 30, 5, 36, 'SOLEart', '', '2015-02-11', 1);
INSERT INTO organisation VALUES (12, 24, 22, 5, 36, 'Soma Linz', '', '2015-02-11', 1);
INSERT INTO organisation VALUES (19, 26, 29, 5, 36, 'SOS Kinderdorf Seekirchen', '', '2015-02-11', 1);
INSERT INTO organisation VALUES (9, 21, 18, 5, 36, 'Wärmestube', '', '2015-02-11', 1);
INSERT INTO organisation VALUES (13, 25, 23, 5, 36, 'SLS Bürmoos', '', '2015-02-11', 1);
INSERT INTO organisation VALUES (22, NULL, NULL, NULL, 36, 'Schwund eingehend', 'Diese Organisation repräsentiert den auftretenden Schwund der bei eingehenden Waren auftreten kann.', '2015-02-12', 1);
INSERT INTO organisation VALUES (23, NULL, NULL, NULL, 36, 'Schwund ausgehend', 'Diese Organisation repräsentiert den auftretenden Schwund, der bei ausgehenden Waren auftreten kann.', '2015-02-12', 1);
INSERT INTO organisation VALUES (8, 19, 21, 5, 37, 'Nannerl GmbH & CO KG', 'Sehr gute Beziehung zu Nannerl. Immer nett und freundlich!', '2015-01-15', 1);


--
-- TOC entry 2319 (class 0 OID 0)
-- Dependencies: 192
-- Name: organisation_organisation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('organisation_organisation_id_seq', 23, true);


--
-- TOC entry 2265 (class 0 OID 16487)
-- Dependencies: 193
-- Data for Name: orgcontactperson; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO orgcontactperson VALUES (47, 8);
INSERT INTO orgcontactperson VALUES (49, 11);
INSERT INTO orgcontactperson VALUES (36, 10);
INSERT INTO orgcontactperson VALUES (50, 12);
INSERT INTO orgcontactperson VALUES (53, 19);
INSERT INTO orgcontactperson VALUES (52, 19);
INSERT INTO orgcontactperson VALUES (48, 9);
INSERT INTO orgcontactperson VALUES (36, 13);


--
-- TOC entry 2247 (class 0 OID 16411)
-- Dependencies: 175
-- Data for Name: outgoing_article; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO outgoing_article VALUES (149, 66, 138, 0, 250);
INSERT INTO outgoing_article VALUES (159, 67, 138, 1, 75);
INSERT INTO outgoing_article VALUES (160, 67, 139, 2, 25);
INSERT INTO outgoing_article VALUES (161, 67, 137, 0, 100);
INSERT INTO outgoing_article VALUES (165, 70, 138, 0, 175);
INSERT INTO outgoing_article VALUES (166, 71, 137, 1, 100);
INSERT INTO outgoing_article VALUES (167, 71, 140, 0, 20);


--
-- TOC entry 2320 (class 0 OID 0)
-- Dependencies: 194
-- Name: outgoing_article_outgoing_article_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('outgoing_article_outgoing_article_id_seq', 167, true);


--
-- TOC entry 2267 (class 0 OID 16492)
-- Dependencies: 195
-- Data for Name: outgoing_delivery; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO outgoing_delivery VALUES (70, 19, 36, 93, 0, '2016-08-01', 'hallo', '2015-02-13', 0);
INSERT INTO outgoing_delivery VALUES (71, 21, 36, NULL, 0, '2015-02-04', '', '2015-02-17', 0);
INSERT INTO outgoing_delivery VALUES (66, 21, 36, 94, 0, '2015-02-10', 'seas', '2015-02-11', 0);
INSERT INTO outgoing_delivery VALUES (67, 19, 36, 94, 0, '2015-02-13', 'klass', '2015-02-12', 0);


--
-- TOC entry 2321 (class 0 OID 0)
-- Dependencies: 196
-- Name: outgoing_delivery_outgoing_delivery_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('outgoing_delivery_outgoing_delivery_id_seq', 71, true);


--
-- TOC entry 2269 (class 0 OID 16502)
-- Dependencies: 197
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO permission VALUES (1, ' ', 'Admin');
INSERT INTO permission VALUES (3, '', 'Read');
INSERT INTO permission VALUES (2, '', 'ReadWrite');


--
-- TOC entry 2322 (class 0 OID 0)
-- Dependencies: 198
-- Name: permission_permission_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('permission_permission_id_seq', 1, false);


--
-- TOC entry 2271 (class 0 OID 16510)
-- Dependencies: 199
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO person VALUES (37, 5, 12, 17, 37, 'Herr', '', 'Stephan', 'Stadlmair', 'is a Student', '2015-01-15', 1);
INSERT INTO person VALUES (47, 5, 21, 18, 36, 'Herr', 'BSc', 'Max', 'Mustermann', 'Ansprechpartner für Nannerl', '2015-01-18', 1);
INSERT INTO person VALUES (36, 5, 6, 2, 36, 'Herr', '', 'Matthias', 'Schnöll', '', '2014-12-08', 1);
INSERT INTO person VALUES (49, 5, NULL, NULL, 36, 'Frau', '', 'Tanja', 'Rakovic', '', '2015-02-02', 1);
INSERT INTO person VALUES (50, NULL, NULL, NULL, 36, 'Herr', '', 'Manfred', 'Kiesenhofer', '', '2015-02-02', 1);
INSERT INTO person VALUES (53, 5, NULL, NULL, 36, 'Frau', '', 'Sabine', 'Paulitsch', '', '2015-02-02', 1);
INSERT INTO person VALUES (54, 5, NULL, NULL, 36, 'Herr', '', 'Seppi', 'Huber', '', '2015-02-04', 1);
INSERT INTO person VALUES (52, 5, NULL, NULL, 36, 'Herr', '', 'Rudi', 'Harner', '', '2015-02-05', 1);
INSERT INTO person VALUES (51, 5, NULL, NULL, 36, 'Herr', '', 'Oskar', 'Kaufmann', '', '2015-02-02', 1);
INSERT INTO person VALUES (48, 5, 18, 20, 36, 'Frau', '', 'Hannelore', 'Bauer', 'Liebenswerte Person', '2015-02-06', 1);


--
-- TOC entry 2323 (class 0 OID 0)
-- Dependencies: 200
-- Name: person_person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('person_person_id_seq', 54, true);


--
-- TOC entry 2273 (class 0 OID 16518)
-- Dependencies: 201
-- Data for Name: platformuser; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO platformuser VALUES (37, 1, 'MqPIAmlF4i2dSb0oR-JzwxG9yZidfKtK-n4AgMspQgpJbfJ3RC6Sg', 's.stadlmair@gmx.at');
INSERT INTO platformuser VALUES (47, 2, 'MqPIAmlF4i2dSb0oR-JzwxG9yZidfKtK-n4AgMspQgpJbfJ3RC6Sg', 'max.mustermann@gmail.com');
INSERT INTO platformuser VALUES (36, 1, 'hwTvvtTVKodetjA0VWLGfN40ENDXE_Q1GL6dU8T3jOx2mDjPeKOzU', 'mschnoell@gmx.net');


--
-- TOC entry 2274 (class 0 OID 16521)
-- Dependencies: 202
-- Data for Name: relorgcat; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO relorgcat VALUES (8, 10);
INSERT INTO relorgcat VALUES (8, 9);
INSERT INTO relorgcat VALUES (11, 10);
INSERT INTO relorgcat VALUES (10, 10);
INSERT INTO relorgcat VALUES (21, 13);
INSERT INTO relorgcat VALUES (21, 16);
INSERT INTO relorgcat VALUES (12, 15);
INSERT INTO relorgcat VALUES (12, 16);
INSERT INTO relorgcat VALUES (19, 14);
INSERT INTO relorgcat VALUES (19, 10);
INSERT INTO relorgcat VALUES (9, 13);
INSERT INTO relorgcat VALUES (9, 17);
INSERT INTO relorgcat VALUES (13, 14);
INSERT INTO relorgcat VALUES (13, 13);
INSERT INTO relorgcat VALUES (13, 16);


--
-- TOC entry 2275 (class 0 OID 16524)
-- Dependencies: 203
-- Data for Name: relorgtype; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO relorgtype VALUES (5, 8);
INSERT INTO relorgtype VALUES (5, 11);
INSERT INTO relorgtype VALUES (5, 10);
INSERT INTO relorgtype VALUES (6, 21);
INSERT INTO relorgtype VALUES (6, 12);
INSERT INTO relorgtype VALUES (6, 19);
INSERT INTO relorgtype VALUES (6, 9);
INSERT INTO relorgtype VALUES (6, 13);
INSERT INTO relorgtype VALUES (5, 22);
INSERT INTO relorgtype VALUES (6, 23);


--
-- TOC entry 2276 (class 0 OID 16527)
-- Dependencies: 204
-- Data for Name: relpersontype; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO relpersontype VALUES (2, 36);
INSERT INTO relpersontype VALUES (1, 54);


--
-- TOC entry 2277 (class 0 OID 16530)
-- Dependencies: 205
-- Data for Name: reporting; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2324 (class 0 OID 0)
-- Dependencies: 206
-- Name: reporting_report_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('reporting_report_id_seq', 1, false);


--
-- TOC entry 2279 (class 0 OID 16538)
-- Dependencies: 207
-- Data for Name: telephone; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO telephone VALUES (64, NULL, 11, '123456');


--
-- TOC entry 2325 (class 0 OID 0)
-- Dependencies: 208
-- Name: telephone_telephone_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('telephone_telephone_id_seq', 64, true);


--
-- TOC entry 2281 (class 0 OID 16543)
-- Dependencies: 209
-- Data for Name: type; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO type VALUES (2, 'Unterstützer');
INSERT INTO type VALUES (1, 'Mitarbeiter');
INSERT INTO type VALUES (4, 'Gast');
INSERT INTO type VALUES (3, 'Mitglied');
INSERT INTO type VALUES (5, 'Lieferant');
INSERT INTO type VALUES (6, 'Kunde');
INSERT INTO type VALUES (7, 'Sponsor');
INSERT INTO type VALUES (10, 'privat');
INSERT INTO type VALUES (11, 'geschäftlich');


--
-- TOC entry 2326 (class 0 OID 0)
-- Dependencies: 210
-- Name: type_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('type_type_id_seq', 1, false);


--
-- TOC entry 2001 (class 2606 OID 16567)
-- Name: pk_address; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY address
    ADD CONSTRAINT pk_address PRIMARY KEY (address_id);


--
-- TOC entry 2004 (class 2606 OID 16569)
-- Name: pk_article; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY article
    ADD CONSTRAINT pk_article PRIMARY KEY (article_id);


--
-- TOC entry 2017 (class 2606 OID 16571)
-- Name: pk_category; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY category
    ADD CONSTRAINT pk_category PRIMARY KEY (category_id);


--
-- TOC entry 2020 (class 2606 OID 16573)
-- Name: pk_city; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY city
    ADD CONSTRAINT pk_city PRIMARY KEY (city_id);


--
-- TOC entry 2023 (class 2606 OID 16575)
-- Name: pk_country; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT pk_country PRIMARY KEY (country_id);


--
-- TOC entry 2026 (class 2606 OID 16577)
-- Name: pk_delivery_list; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY delivery_list
    ADD CONSTRAINT pk_delivery_list PRIMARY KEY (delivery_list_id);


--
-- TOC entry 2030 (class 2606 OID 16579)
-- Name: pk_email; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY email
    ADD CONSTRAINT pk_email PRIMARY KEY (email_id);


--
-- TOC entry 2009 (class 2606 OID 16581)
-- Name: pk_incoming_article; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY incoming_article
    ADD CONSTRAINT pk_incoming_article PRIMARY KEY (incoming_article_id);


--
-- TOC entry 2036 (class 2606 OID 16583)
-- Name: pk_incoming_delivery; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY incoming_delivery
    ADD CONSTRAINT pk_incoming_delivery PRIMARY KEY (incoming_delivery_id);


--
-- TOC entry 2040 (class 2606 OID 16585)
-- Name: pk_logging; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY logging
    ADD CONSTRAINT pk_logging PRIMARY KEY (logging_id);


--
-- TOC entry 2043 (class 2606 OID 16587)
-- Name: pk_organisation; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT pk_organisation PRIMARY KEY (organisation_id);


--
-- TOC entry 2052 (class 2606 OID 16589)
-- Name: pk_orgcontactperson; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY orgcontactperson
    ADD CONSTRAINT pk_orgcontactperson PRIMARY KEY (person_id, organisation_id);


--
-- TOC entry 2014 (class 2606 OID 16591)
-- Name: pk_outgoing_article; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY outgoing_article
    ADD CONSTRAINT pk_outgoing_article PRIMARY KEY (outgoing_article_id);


--
-- TOC entry 2056 (class 2606 OID 16593)
-- Name: pk_outgoing_delivery; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY outgoing_delivery
    ADD CONSTRAINT pk_outgoing_delivery PRIMARY KEY (outgoing_delivery_id);


--
-- TOC entry 2061 (class 2606 OID 16595)
-- Name: pk_permission; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT pk_permission PRIMARY KEY (permission_id);


--
-- TOC entry 2066 (class 2606 OID 16597)
-- Name: pk_person; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT pk_person PRIMARY KEY (person_id);


--
-- TOC entry 2070 (class 2606 OID 16599)
-- Name: pk_platformuser; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY platformuser
    ADD CONSTRAINT pk_platformuser PRIMARY KEY (person_id);


--
-- TOC entry 2074 (class 2606 OID 16601)
-- Name: pk_relorgcat; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY relorgcat
    ADD CONSTRAINT pk_relorgcat PRIMARY KEY (organisation_id, category_id);


--
-- TOC entry 2079 (class 2606 OID 16603)
-- Name: pk_relorgtype; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY relorgtype
    ADD CONSTRAINT pk_relorgtype PRIMARY KEY (type_id, organisation_id);


--
-- TOC entry 2084 (class 2606 OID 16605)
-- Name: pk_relpersontype; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY relpersontype
    ADD CONSTRAINT pk_relpersontype PRIMARY KEY (type_id, person_id);


--
-- TOC entry 2089 (class 2606 OID 16607)
-- Name: pk_reporting; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY reporting
    ADD CONSTRAINT pk_reporting PRIMARY KEY (report_id);


--
-- TOC entry 2092 (class 2606 OID 16609)
-- Name: pk_telephone; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY telephone
    ADD CONSTRAINT pk_telephone PRIMARY KEY (telephone_id);


--
-- TOC entry 2097 (class 2606 OID 16611)
-- Name: pk_type; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY type
    ADD CONSTRAINT pk_type PRIMARY KEY (type_id);


--
-- TOC entry 1999 (class 1259 OID 16612)
-- Name: address_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX address_pk ON address USING btree (address_id);


--
-- TOC entry 2002 (class 1259 OID 16613)
-- Name: article_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX article_pk ON article USING btree (article_id);


--
-- TOC entry 2015 (class 1259 OID 16614)
-- Name: category_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX category_pk ON category USING btree (category_id);


--
-- TOC entry 2018 (class 1259 OID 16615)
-- Name: city_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX city_pk ON city USING btree (city_id);


--
-- TOC entry 2021 (class 1259 OID 16616)
-- Name: country_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX country_pk ON country USING btree (country_id);


--
-- TOC entry 2033 (class 1259 OID 16617)
-- Name: delivers_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX delivers_fk ON incoming_delivery USING btree (organisation_id);


--
-- TOC entry 2024 (class 1259 OID 16618)
-- Name: delivery_list_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX delivery_list_pk ON delivery_list USING btree (delivery_list_id);


--
-- TOC entry 2028 (class 1259 OID 16619)
-- Name: email_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX email_pk ON email USING btree (email_id);


--
-- TOC entry 2005 (class 1259 OID 16620)
-- Name: inarticle_indelivery_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX inarticle_indelivery_fk ON incoming_article USING btree (incoming_delivery_id);


--
-- TOC entry 2006 (class 1259 OID 16621)
-- Name: incoming_article_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX incoming_article_pk ON incoming_article USING btree (incoming_article_id);


--
-- TOC entry 2034 (class 1259 OID 16622)
-- Name: incoming_delivery_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX incoming_delivery_pk ON incoming_delivery USING btree (incoming_delivery_id);


--
-- TOC entry 2007 (class 1259 OID 16623)
-- Name: incomingarticle_article_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX incomingarticle_article_fk ON incoming_article USING btree (article_id);


--
-- TOC entry 2038 (class 1259 OID 16624)
-- Name: logging_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX logging_pk ON logging USING btree (logging_id);


--
-- TOC entry 2041 (class 1259 OID 16625)
-- Name: organisation_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX organisation_pk ON organisation USING btree (organisation_id);


--
-- TOC entry 2048 (class 1259 OID 16626)
-- Name: orgcontactperson2_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX orgcontactperson2_fk ON orgcontactperson USING btree (organisation_id);


--
-- TOC entry 2049 (class 1259 OID 16627)
-- Name: orgcontactperson_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX orgcontactperson_fk ON orgcontactperson USING btree (person_id);


--
-- TOC entry 2050 (class 1259 OID 16628)
-- Name: orgcontactperson_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX orgcontactperson_pk ON orgcontactperson USING btree (person_id, organisation_id);


--
-- TOC entry 2010 (class 1259 OID 16629)
-- Name: outarticle_outdelivery_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX outarticle_outdelivery_fk ON outgoing_article USING btree (outgoing_delivery_id);


--
-- TOC entry 2053 (class 1259 OID 16630)
-- Name: outdelivery_deliverylist_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX outdelivery_deliverylist_fk ON outgoing_delivery USING btree (delivery_list_id);


--
-- TOC entry 2011 (class 1259 OID 16631)
-- Name: outgoing_article_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX outgoing_article_pk ON outgoing_article USING btree (outgoing_article_id);


--
-- TOC entry 2054 (class 1259 OID 16632)
-- Name: outgoing_delivery_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX outgoing_delivery_pk ON outgoing_delivery USING btree (outgoing_delivery_id);


--
-- TOC entry 2012 (class 1259 OID 16633)
-- Name: outgoingarticle_article_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX outgoingarticle_article_fk ON outgoing_article USING btree (article_id);


--
-- TOC entry 2059 (class 1259 OID 16634)
-- Name: permission_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX permission_pk ON permission USING btree (permission_id);


--
-- TOC entry 2062 (class 1259 OID 16635)
-- Name: person_city_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX person_city_fk ON person USING btree (city_id);


--
-- TOC entry 2063 (class 1259 OID 16636)
-- Name: person_country_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX person_country_fk ON person USING btree (country_id);


--
-- TOC entry 2064 (class 1259 OID 16637)
-- Name: person_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX person_pk ON person USING btree (person_id);


--
-- TOC entry 2071 (class 1259 OID 16638)
-- Name: platformuser_permission_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX platformuser_permission_fk ON platformuser USING btree (permission_id);


--
-- TOC entry 2072 (class 1259 OID 16639)
-- Name: platformuser_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX platformuser_pk ON platformuser USING btree (person_id);


--
-- TOC entry 2057 (class 1259 OID 16640)
-- Name: receives_delivery_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX receives_delivery_fk ON outgoing_delivery USING btree (organisation_id);


--
-- TOC entry 2031 (class 1259 OID 16641)
-- Name: relemailtype_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relemailtype_fk ON email USING btree (type_id);


--
-- TOC entry 2044 (class 1259 OID 16642)
-- Name: relorgaddress_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relorgaddress_fk ON organisation USING btree (address_id);


--
-- TOC entry 2075 (class 1259 OID 16643)
-- Name: relorgcat2_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relorgcat2_fk ON relorgcat USING btree (category_id);


--
-- TOC entry 2076 (class 1259 OID 16644)
-- Name: relorgcat_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relorgcat_fk ON relorgcat USING btree (organisation_id);


--
-- TOC entry 2077 (class 1259 OID 16645)
-- Name: relorgcat_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX relorgcat_pk ON relorgcat USING btree (organisation_id, category_id);


--
-- TOC entry 2045 (class 1259 OID 16646)
-- Name: relorgcity_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relorgcity_fk ON organisation USING btree (city_id);


--
-- TOC entry 2046 (class 1259 OID 16647)
-- Name: relorgcountry_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relorgcountry_fk ON organisation USING btree (country_id);


--
-- TOC entry 2080 (class 1259 OID 16648)
-- Name: relorgtype2_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relorgtype2_fk ON relorgtype USING btree (organisation_id);


--
-- TOC entry 2081 (class 1259 OID 16649)
-- Name: relorgtype_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relorgtype_fk ON relorgtype USING btree (type_id);


--
-- TOC entry 2082 (class 1259 OID 16650)
-- Name: relorgtype_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX relorgtype_pk ON relorgtype USING btree (type_id, organisation_id);


--
-- TOC entry 2067 (class 1259 OID 16651)
-- Name: relpersonaddress_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relpersonaddress_fk ON person USING btree (address_id);


--
-- TOC entry 2032 (class 1259 OID 16652)
-- Name: relpersonemail_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relpersonemail_fk ON email USING btree (person_id);


--
-- TOC entry 2093 (class 1259 OID 16653)
-- Name: relpersontelephone_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relpersontelephone_fk ON telephone USING btree (person_id);


--
-- TOC entry 2085 (class 1259 OID 16654)
-- Name: relpersontype2_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relpersontype2_fk ON relpersontype USING btree (person_id);


--
-- TOC entry 2086 (class 1259 OID 16655)
-- Name: relpersontype_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relpersontype_fk ON relpersontype USING btree (type_id);


--
-- TOC entry 2087 (class 1259 OID 16656)
-- Name: relpersontype_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX relpersontype_pk ON relpersontype USING btree (type_id, person_id);


--
-- TOC entry 2068 (class 1259 OID 16657)
-- Name: relpersonupdateperson_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX relpersonupdateperson_fk ON person USING btree (per_person_id);


--
-- TOC entry 2094 (class 1259 OID 16658)
-- Name: reltelephonetype_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX reltelephonetype_fk ON telephone USING btree (type_id);


--
-- TOC entry 2090 (class 1259 OID 16659)
-- Name: reporting_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX reporting_pk ON reporting USING btree (report_id);


--
-- TOC entry 2095 (class 1259 OID 16660)
-- Name: telephone_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX telephone_pk ON telephone USING btree (telephone_id);


--
-- TOC entry 2098 (class 1259 OID 16661)
-- Name: type_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX type_pk ON type USING btree (type_id);


--
-- TOC entry 2047 (class 1259 OID 16662)
-- Name: updated_organisation_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX updated_organisation_fk ON organisation USING btree (person_id);


--
-- TOC entry 2027 (class 1259 OID 16663)
-- Name: updates_deliverylist_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX updates_deliverylist_fk ON delivery_list USING btree (person_id);


--
-- TOC entry 2037 (class 1259 OID 16664)
-- Name: updates_incoming_delivery_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX updates_incoming_delivery_fk ON incoming_delivery USING btree (person_id);


--
-- TOC entry 2058 (class 1259 OID 16665)
-- Name: updatesoutgoingdelivery_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX updatesoutgoingdelivery_fk ON outgoing_delivery USING btree (person_id);


--
-- TOC entry 2103 (class 2606 OID 16666)
-- Name: fk_delivery_updates_d_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY delivery_list
    ADD CONSTRAINT fk_delivery_updates_d_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2104 (class 2606 OID 16671)
-- Name: fk_email_relemailt_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY email
    ADD CONSTRAINT fk_email_relemailt_type FOREIGN KEY (type_id) REFERENCES type(type_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2105 (class 2606 OID 16676)
-- Name: fk_email_relperson_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY email
    ADD CONSTRAINT fk_email_relperson_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 2106 (class 2606 OID 16681)
-- Name: fk_incoming_delivers_organisa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY incoming_delivery
    ADD CONSTRAINT fk_incoming_delivers_organisa FOREIGN KEY (organisation_id) REFERENCES organisation(organisation_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2099 (class 2606 OID 16686)
-- Name: fk_incoming_inarticle_incoming; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY incoming_article
    ADD CONSTRAINT fk_incoming_inarticle_incoming FOREIGN KEY (incoming_delivery_id) REFERENCES incoming_delivery(incoming_delivery_id) ON UPDATE RESTRICT ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED;


--
-- TOC entry 2100 (class 2606 OID 16691)
-- Name: fk_incoming_incominga_article; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY incoming_article
    ADD CONSTRAINT fk_incoming_incominga_article FOREIGN KEY (article_id) REFERENCES article(article_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2107 (class 2606 OID 16696)
-- Name: fk_incoming_updates_i_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY incoming_delivery
    ADD CONSTRAINT fk_incoming_updates_i_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2108 (class 2606 OID 16701)
-- Name: fk_organisa_relorgadd_address; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT fk_organisa_relorgadd_address FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2109 (class 2606 OID 16706)
-- Name: fk_organisa_relorgcit_city; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT fk_organisa_relorgcit_city FOREIGN KEY (city_id) REFERENCES city(city_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2110 (class 2606 OID 16711)
-- Name: fk_organisa_relorgcou_country; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT fk_organisa_relorgcou_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2111 (class 2606 OID 16716)
-- Name: fk_organisa_updated_o_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT fk_organisa_updated_o_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2112 (class 2606 OID 16721)
-- Name: fk_orgconta_orgcontac_organisa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orgcontactperson
    ADD CONSTRAINT fk_orgconta_orgcontac_organisa FOREIGN KEY (organisation_id) REFERENCES organisation(organisation_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2113 (class 2606 OID 16726)
-- Name: fk_orgconta_orgcontac_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orgcontactperson
    ADD CONSTRAINT fk_orgconta_orgcontac_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2101 (class 2606 OID 16731)
-- Name: fk_outgoing_outarticl_outgoing; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outgoing_article
    ADD CONSTRAINT fk_outgoing_outarticl_outgoing FOREIGN KEY (outgoing_delivery_id) REFERENCES outgoing_delivery(outgoing_delivery_id) ON UPDATE RESTRICT ON DELETE RESTRICT DEFERRABLE INITIALLY DEFERRED;


--
-- TOC entry 2114 (class 2606 OID 16736)
-- Name: fk_outgoing_outdelive_delivery; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outgoing_delivery
    ADD CONSTRAINT fk_outgoing_outdelive_delivery FOREIGN KEY (delivery_list_id) REFERENCES delivery_list(delivery_list_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2102 (class 2606 OID 16741)
-- Name: fk_outgoing_outgoinga_article; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outgoing_article
    ADD CONSTRAINT fk_outgoing_outgoinga_article FOREIGN KEY (article_id) REFERENCES article(article_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2115 (class 2606 OID 16746)
-- Name: fk_outgoing_receives__organisa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outgoing_delivery
    ADD CONSTRAINT fk_outgoing_receives__organisa FOREIGN KEY (organisation_id) REFERENCES organisation(organisation_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2116 (class 2606 OID 16751)
-- Name: fk_outgoing_updatesou_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY outgoing_delivery
    ADD CONSTRAINT fk_outgoing_updatesou_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2117 (class 2606 OID 16756)
-- Name: fk_person_person_ci_city; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT fk_person_person_ci_city FOREIGN KEY (city_id) REFERENCES city(city_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2118 (class 2606 OID 16761)
-- Name: fk_person_person_co_country; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT fk_person_person_co_country FOREIGN KEY (country_id) REFERENCES country(country_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2119 (class 2606 OID 16766)
-- Name: fk_person_relperson_address; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT fk_person_relperson_address FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2120 (class 2606 OID 16771)
-- Name: fk_person_relperson_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT fk_person_relperson_person FOREIGN KEY (per_person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2121 (class 2606 OID 16776)
-- Name: fk_platform_platformu_permissi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY platformuser
    ADD CONSTRAINT fk_platform_platformu_permissi FOREIGN KEY (permission_id) REFERENCES permission(permission_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2122 (class 2606 OID 16781)
-- Name: fk_platform_relperson_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY platformuser
    ADD CONSTRAINT fk_platform_relperson_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2123 (class 2606 OID 16786)
-- Name: fk_relorgca_relorgcat_category; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relorgcat
    ADD CONSTRAINT fk_relorgca_relorgcat_category FOREIGN KEY (category_id) REFERENCES category(category_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 2124 (class 2606 OID 16791)
-- Name: fk_relorgca_relorgcat_organisa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relorgcat
    ADD CONSTRAINT fk_relorgca_relorgcat_organisa FOREIGN KEY (organisation_id) REFERENCES organisation(organisation_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 2125 (class 2606 OID 16796)
-- Name: fk_relorgty_relorgtyp_organisa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relorgtype
    ADD CONSTRAINT fk_relorgty_relorgtyp_organisa FOREIGN KEY (organisation_id) REFERENCES organisation(organisation_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 2126 (class 2606 OID 16801)
-- Name: fk_relorgty_relorgtyp_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relorgtype
    ADD CONSTRAINT fk_relorgty_relorgtyp_type FOREIGN KEY (type_id) REFERENCES type(type_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2127 (class 2606 OID 16806)
-- Name: fk_relperso_relperson_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relpersontype
    ADD CONSTRAINT fk_relperso_relperson_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 2128 (class 2606 OID 16811)
-- Name: fk_relperso_relperson_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY relpersontype
    ADD CONSTRAINT fk_relperso_relperson_type FOREIGN KEY (type_id) REFERENCES type(type_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2129 (class 2606 OID 16816)
-- Name: fk_telephon_relperson_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telephone
    ADD CONSTRAINT fk_telephon_relperson_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 2130 (class 2606 OID 16821)
-- Name: fk_telephon_relteleph_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telephone
    ADD CONSTRAINT fk_telephon_relteleph_type FOREIGN KEY (type_id) REFERENCES type(type_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2289 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-02-17 11:23:35

--
-- PostgreSQL database dump complete
--

