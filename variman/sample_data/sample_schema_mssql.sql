
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO

/****** Object:  Table [dbo].[actagt] ******/
CREATE TABLE [dbo].[actagt](
	[office_id] [varchar](7) NULL,
	[agent_id] [varchar](7) NOT NULL,
	[r_md] [datetime] NOT NULL,
 CONSTRAINT [actagt_pkey] PRIMARY KEY CLUSTERED 
(
	[agent_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[agt] ******/
CREATE TABLE [dbo].[agt](
	[cell_phone] [varchar](11) NULL,
	[office_id] [varchar](7) NULL,
	[first_name] [varchar](12) NULL,
	[agent_id] [varchar](7) NOT NULL,
	[last_name] [varchar](18) NULL,
	[dsgnations] [varchar](20) NULL,
	[url] [varchar](40) NULL,
	[off_phone] [varchar](11) NULL,
	[password] [varchar](12) NULL,
	[r_md] [datetime] NOT NULL,
 CONSTRAINT [agt_pkey] PRIMARY KEY CLUSTERED 
(
	[agent_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[con] ******/
CREATE TABLE [dbo].[con](
	[owner] [varchar](20) NULL,
	[stnum] [varchar](10) NULL,
	[ld] [datetime] NULL,
	[broker] [varchar](40) NULL,
	[vew] [bigint] NULL,
	[ltyp] [varchar](4) NULL,
	[status] [varchar](1) NULL,
	[sqft] [varchar](8) NULL,
	[m_school] [int] NULL,
	[h_school] [int] NULL,
	[stname] [varchar](20) NULL,
	[lp] [int] NULL,
	[lif] [varchar](100) NULL,
	[e_school] [int] NULL,
	[ar] [varchar](5) NULL,
	[ln] [varchar](40) NOT NULL,
	[ef] [varchar](100) NULL,
	[url] [varchar](40) NULL,
	[zip_code] [bigint] NULL,
	[agent_id] [varchar](6) NULL,
	[r_md] [datetime] NOT NULL,
 CONSTRAINT [con_pkey] PRIMARY KEY CLUSTERED 
(
	[ln] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[lnd] ******/
CREATE TABLE [dbo].[lnd](
	[vew] [bigint] NULL,
	[sqft] [varchar](8) NULL,
	[zip_code] [bigint] NULL,
	[ln] [varchar](40) NOT NULL,
	[ld] [datetime] NULL,
	[ltyp] [varchar](4) NULL,
	[ar] [varchar](5) NULL,
	[agent_id] [varchar](6) NULL,
	[broker] [varchar](40) NULL,
	[lp] [int] NULL,
	[owner] [varchar](20) NULL,
	[url] [varchar](40) NULL,
	[m_school] [int] NULL,
	[lif] [varchar](100) NULL,
	[ef] [varchar](100) NULL,
	[e_school] [int] NULL,
	[h_school] [int] NULL,
	[stname] [varchar](20) NULL,
	[status] [varchar](1) NULL,
	[stnum] [varchar](10) NULL,
	[r_md] [datetime] NOT NULL,
 CONSTRAINT [lnd_pkey] PRIMARY KEY CLUSTERED 
(
	[ln] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[mob] ******/
CREATE TABLE [dbo].[mob](
	[ld] [datetime] NULL,
	[stnum] [varchar](10) NULL,
	[owner] [varchar](20) NULL,
	[ltyp] [varchar](4) NULL,
	[h_school] [int] NULL,
	[sqft] [varchar](8) NULL,
	[zip_code] [bigint] NULL,
	[ef] [varchar](100) NULL,
	[ar] [varchar](5) NULL,
	[ln] [varchar](40) NOT NULL,
	[lif] [varchar](100) NULL,
	[status] [varchar](1) NULL,
	[stname] [varchar](20) NULL,
	[e_school] [int] NULL,
	[m_school] [int] NULL,
	[url] [varchar](40) NULL,
	[agent_id] [varchar](6) NULL,
	[lp] [int] NULL,
	[vew] [bigint] NULL,
	[broker] [varchar](40) NULL,
	[r_md] [datetime] NOT NULL,
 CONSTRAINT [mob_pkey] PRIMARY KEY CLUSTERED 
(
	[ln] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[mul] ******/
CREATE TABLE [dbo].[mul](
	[ln] [varchar](40) NOT NULL,
	[lif] [varchar](100) NULL,
	[stnum] [varchar](10) NULL,
	[url] [varchar](40) NULL,
	[ld] [datetime] NULL,
	[h_school] [int] NULL,
	[m_school] [int] NULL,
	[owner] [varchar](20) NULL,
	[ef] [varchar](100) NULL,
	[broker] [varchar](40) NULL,
	[vew] [bigint] NULL,
	[lp] [int] NULL,
	[zip_code] [bigint] NULL,
	[sqft] [varchar](8) NULL,
	[ltyp] [varchar](4) NULL,
	[stname] [varchar](20) NULL,
	[e_school] [int] NULL,
	[agent_id] [varchar](6) NULL,
	[status] [varchar](1) NULL,
	[ar] [varchar](5) NULL,
	[r_md] [datetime] NOT NULL,
 CONSTRAINT [mul_pkey] PRIMARY KEY CLUSTERED 
(
	[ln] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[office] ******/
CREATE TABLE [dbo].[office](
	[url] [varchar](40) NULL,
	[name] [varchar](20) NULL,
	[broker_id] [varchar](6) NULL,
	[office_id] [varchar](7) NOT NULL,
	[r_md] [datetime] NOT NULL,
 CONSTRAINT [office_pkey] PRIMARY KEY CLUSTERED 
(
	[office_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[rets_property_res] ******/
CREATE TABLE [dbo].[rets_property_res](
	[r_ld] [datetime] NULL,
	[r_md] [datetime] NULL,
	[r_lp] [int] NULL,
	[r_baths] [int] NULL,
	[r_beds] [int] NULL,
	[r_agent_id] [varchar](12) NULL,
	[r_city] [varchar](24) NULL,
	[r_county] [varchar](24) NULL,
	[r_ln] [varchar](40) NOT NULL,
	[r_office_id] [varchar](12) NULL,
	[r_zip_code] [varchar](10) NULL,
	[r_state] [varchar](24) NULL,
	[r_stnum] [varchar](12) NULL,
	[r_street_dir_prefix] [varchar](12) NULL,
	[r_stname] [varchar](24) NULL,
	[r_ltyp] [varchar](4) NULL,
	[r_board] [varchar](24) NULL,
	[r_cldate] [datetime] NULL,
	[r_codate] [datetime] NULL,
	[r_cp] [int] NULL,
	[r_unnum] [varchar](12) NULL,
	[r_ls] [varchar](12) NULL,
	[r_garage] [int] NULL,
	[r_area] [int] NULL,
	[r_rooms] [int] NULL,
	[r_year_built] [int] NULL,
	[r_sqft] [float] NULL,
	[r_thumb_md] [datetime] NULL,
	[r_thumbnails] [int] NULL,
	[r_photo_md] [datetime] NULL,
	[r_photos] [int] NULL
 CONSTRAINT [rets_property_res_pkey] PRIMARY KEY CLUSTERED 
(
	[r_ln] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[xceligent] ******/
CREATE TABLE [dbo].[xceligent](
	[property_id] [char](10) NULL,
	[property_name] [char](80) NULL,
	[address] [char](40) NULL,
	[building_number] [char](20) NULL,
	[city] [char](40) NULL,
	[state] [char](40) NULL,
	[county] [char](40) NULL,
	[zip] [char](20) NULL,
	[metro] [char](60) NULL,
	[general_use] [char](20) NULL,
	[gba] [char](20) NULL,
	[gla] [char](20) NULL,
	[nra] [char](20) NULL,
	[building_size] [char](20) NULL,
	[no_of_floors] [char](10) NULL,
	[construction_status] [char](20) NULL,
	[construction_class] [char](20) NULL,
	[year_built] [char](10) NULL,
	[listing_type] [char](30) NULL,
	[sale_created_date] [char](20) NULL,
	[sale_modified_date] [char](20) NULL,
	[sale_listing_status] [char](20) NULL,
	[sale_sharing_level] [char](10) NULL,
	[sale_agent_id] [char](20) NULL,
	[sale_agent_first_name] [char](60) NULL,
	[sale_agent_last_name] [char](60) NULL,
	[sale_email_address] [char](40) NULL,
	[sale_company_name] [char](60) NULL,
	[sale_agent_address] [char](80) NULL,
	[sale_agent_city] [char](40) NULL,
	[sale_agent_state] [char](20) NULL,
	[sale_agent_zip] [char](20) NULL,
	[specific_use] [char](60) NULL,
	[tenancy_type] [char](40) NULL,
	[sale_asking_price] [char](80) NULL,
	[sale_agent_phone_number] [char](40) NULL,
	[r_md] [datetime] NOT NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF


