package com.sim.test.bean;

import java.util.List;

/**
 * Created by Grugsum on 2019/4/22.
 */

public class SmzdmDataBean {

    private String error_code;
    private String error_msg;
    private String smzdm_id;
    private String s;
    private DataBean data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getSmzdm_id() {
        return smzdm_id;
    }

    public void setSmzdm_id(String smzdm_id) {
        this.smzdm_id = smzdm_id;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String source;
        private BannerBean banner;
        private Object widget;
        private String title;
        private String past_num;
        private String start_rule;
        private String updated_num;
        private String is_new_user;
        private String is_new_active;
        private List <RowsBean> rows;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public BannerBean getBanner() {
            return banner;
        }

        public void setBanner(BannerBean banner) {
            this.banner = banner;
        }

        public Object getWidget() {
            return widget;
        }

        public void setWidget(Object widget) {
            this.widget = widget;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPast_num() {
            return past_num;
        }

        public void setPast_num(String past_num) {
            this.past_num = past_num;
        }

        public String getStart_rule() {
            return start_rule;
        }

        public void setStart_rule(String start_rule) {
            this.start_rule = start_rule;
        }

        public String getUpdated_num() {
            return updated_num;
        }

        public void setUpdated_num(String updated_num) {
            this.updated_num = updated_num;
        }

        public String getIs_new_user() {
            return is_new_user;
        }

        public void setIs_new_user(String is_new_user) {
            this.is_new_user = is_new_user;
        }

        public String getIs_new_active() {
            return is_new_active;
        }

        public void setIs_new_active(String is_new_active) {
            this.is_new_active = is_new_active;
        }

        public List <RowsBean> getRows() {
            return rows;
        }

        public void setRows(List <RowsBean> rows) {
            this.rows = rows;
        }

        public static class BannerBean {

            private TonglanBannerBean tonglan_banner;
            private LittleBannerOptionsBean little_banner_options;
            private List <BigBannerBean> big_banner;
            private List <LittleBannerBean> little_banner;

            public TonglanBannerBean getTonglan_banner() {
                return tonglan_banner;
            }

            public void setTonglan_banner(TonglanBannerBean tonglan_banner) {
                this.tonglan_banner = tonglan_banner;
            }

            public LittleBannerOptionsBean getLittle_banner_options() {
                return little_banner_options;
            }

            public void setLittle_banner_options(LittleBannerOptionsBean little_banner_options) {
                this.little_banner_options = little_banner_options;
            }

            public List <BigBannerBean> getBig_banner() {
                return big_banner;
            }

            public void setBig_banner(List <BigBannerBean> big_banner) {
                this.big_banner = big_banner;
            }

            public List <LittleBannerBean> getLittle_banner() {
                return little_banner;
            }

            public void setLittle_banner(List <LittleBannerBean> little_banner) {
                this.little_banner = little_banner;
            }

            public static class TonglanBannerBean {

                private String title;
                private String sub_title;
                private String img;
                private RedirectDataBean redirect_data;
                private String link;
                private String logo;
                private String logo_title;
                private String id;
                private String color_card;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getSub_title() {
                    return sub_title;
                }

                public void setSub_title(String sub_title) {
                    this.sub_title = sub_title;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public RedirectDataBean getRedirect_data() {
                    return redirect_data;
                }

                public void setRedirect_data(RedirectDataBean redirect_data) {
                    this.redirect_data = redirect_data;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public String getLogo_title() {
                    return logo_title;
                }

                public void setLogo_title(String logo_title) {
                    this.logo_title = logo_title;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getColor_card() {
                    return color_card;
                }

                public void setColor_card(String color_card) {
                    this.color_card = color_card;
                }

                public static class RedirectDataBean {

                    private String link;
                    private String link_type;
                    private String sub_type;
                    private String link_val;
                    private String link_title;
                    private String isv_code_second;
                    private String jd_isv_code;
                    private String wiki_id;

                    public String getLink() {
                        return link;
                    }

                    public void setLink(String link) {
                        this.link = link;
                    }

                    public String getLink_type() {
                        return link_type;
                    }

                    public void setLink_type(String link_type) {
                        this.link_type = link_type;
                    }

                    public String getSub_type() {
                        return sub_type;
                    }

                    public void setSub_type(String sub_type) {
                        this.sub_type = sub_type;
                    }

                    public String getLink_val() {
                        return link_val;
                    }

                    public void setLink_val(String link_val) {
                        this.link_val = link_val;
                    }

                    public String getLink_title() {
                        return link_title;
                    }

                    public void setLink_title(String link_title) {
                        this.link_title = link_title;
                    }

                    public String getIsv_code_second() {
                        return isv_code_second;
                    }

                    public void setIsv_code_second(String isv_code_second) {
                        this.isv_code_second = isv_code_second;
                    }

                    public String getJd_isv_code() {
                        return jd_isv_code;
                    }

                    public void setJd_isv_code(String jd_isv_code) {
                        this.jd_isv_code = jd_isv_code;
                    }

                    public String getWiki_id() {
                        return wiki_id;
                    }

                    public void setWiki_id(String wiki_id) {
                        this.wiki_id = wiki_id;
                    }
                }
            }

            public static class LittleBannerOptionsBean {

                private String backgroud;
                private String color_card;
                private String end_date;
                private String start_date;
                private String pic_url;
                private String img;
                private String img_w;
                private String img_h;

                public String getBackgroud() {
                    return backgroud;
                }

                public void setBackgroud(String backgroud) {
                    this.backgroud = backgroud;
                }

                public String getColor_card() {
                    return color_card;
                }

                public void setColor_card(String color_card) {
                    this.color_card = color_card;
                }

                public String getEnd_date() {
                    return end_date;
                }

                public void setEnd_date(String end_date) {
                    this.end_date = end_date;
                }

                public String getStart_date() {
                    return start_date;
                }

                public void setStart_date(String start_date) {
                    this.start_date = start_date;
                }

                public String getPic_url() {
                    return pic_url;
                }

                public void setPic_url(String pic_url) {
                    this.pic_url = pic_url;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getImg_w() {
                    return img_w;
                }

                public void setImg_w(String img_w) {
                    this.img_w = img_w;
                }

                public String getImg_h() {
                    return img_h;
                }

                public void setImg_h(String img_h) {
                    this.img_h = img_h;
                }
            }

            public static class BigBannerBean {

                private String id;
                private String article_pic;
                private String img;
                private String df_default_url;
                private String article_title;
                private String title;
                private String link;
                private String promotion_type;
                private String source_from;
                private String logo;
                private String logo_title;
                private String article_channel_id;
                private String article_id;
                private String atp;
                private RedirectDataBeanX redirect_data;
                private String channel;
                private List <?> click_tracking_url;
                private List <?> impression_tracking_url;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getArticle_pic() {
                    return article_pic;
                }

                public void setArticle_pic(String article_pic) {
                    this.article_pic = article_pic;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getDf_default_url() {
                    return df_default_url;
                }

                public void setDf_default_url(String df_default_url) {
                    this.df_default_url = df_default_url;
                }

                public String getArticle_title() {
                    return article_title;
                }

                public void setArticle_title(String article_title) {
                    this.article_title = article_title;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getPromotion_type() {
                    return promotion_type;
                }

                public void setPromotion_type(String promotion_type) {
                    this.promotion_type = promotion_type;
                }

                public String getSource_from() {
                    return source_from;
                }

                public void setSource_from(String source_from) {
                    this.source_from = source_from;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public String getLogo_title() {
                    return logo_title;
                }

                public void setLogo_title(String logo_title) {
                    this.logo_title = logo_title;
                }

                public String getArticle_channel_id() {
                    return article_channel_id;
                }

                public void setArticle_channel_id(String article_channel_id) {
                    this.article_channel_id = article_channel_id;
                }

                public String getArticle_id() {
                    return article_id;
                }

                public void setArticle_id(String article_id) {
                    this.article_id = article_id;
                }

                public String getAtp() {
                    return atp;
                }

                public void setAtp(String atp) {
                    this.atp = atp;
                }

                public RedirectDataBeanX getRedirect_data() {
                    return redirect_data;
                }

                public void setRedirect_data(RedirectDataBeanX redirect_data) {
                    this.redirect_data = redirect_data;
                }

                public String getChannel() {
                    return channel;
                }

                public void setChannel(String channel) {
                    this.channel = channel;
                }

                public List <?> getClick_tracking_url() {
                    return click_tracking_url;
                }

                public void setClick_tracking_url(List <?> click_tracking_url) {
                    this.click_tracking_url = click_tracking_url;
                }

                public List <?> getImpression_tracking_url() {
                    return impression_tracking_url;
                }

                public void setImpression_tracking_url(List <?> impression_tracking_url) {
                    this.impression_tracking_url = impression_tracking_url;
                }

                public static class RedirectDataBeanX {

                    private String link;
                    private String link_type;
                    private String sub_type;
                    private String link_val;
                    private String link_title;
                    private String isv_code_second;
                    private String jd_isv_code;
                    private String wiki_id;
                    private String share_img;
                    private String share_title;
                    private String new_article_url;
                    private String zdm_share_type;
                    private String md5_url;

                    public String getLink() {
                        return link;
                    }

                    public void setLink(String link) {
                        this.link = link;
                    }

                    public String getLink_type() {
                        return link_type;
                    }

                    public void setLink_type(String link_type) {
                        this.link_type = link_type;
                    }

                    public String getSub_type() {
                        return sub_type;
                    }

                    public void setSub_type(String sub_type) {
                        this.sub_type = sub_type;
                    }

                    public String getLink_val() {
                        return link_val;
                    }

                    public void setLink_val(String link_val) {
                        this.link_val = link_val;
                    }

                    public String getLink_title() {
                        return link_title;
                    }

                    public void setLink_title(String link_title) {
                        this.link_title = link_title;
                    }

                    public String getIsv_code_second() {
                        return isv_code_second;
                    }

                    public void setIsv_code_second(String isv_code_second) {
                        this.isv_code_second = isv_code_second;
                    }

                    public String getJd_isv_code() {
                        return jd_isv_code;
                    }

                    public void setJd_isv_code(String jd_isv_code) {
                        this.jd_isv_code = jd_isv_code;
                    }

                    public String getWiki_id() {
                        return wiki_id;
                    }

                    public void setWiki_id(String wiki_id) {
                        this.wiki_id = wiki_id;
                    }

                    public String getShare_img() {
                        return share_img;
                    }

                    public void setShare_img(String share_img) {
                        this.share_img = share_img;
                    }

                    public String getShare_title() {
                        return share_title;
                    }

                    public void setShare_title(String share_title) {
                        this.share_title = share_title;
                    }

                    public String getNew_article_url() {
                        return new_article_url;
                    }

                    public void setNew_article_url(String new_article_url) {
                        this.new_article_url = new_article_url;
                    }

                    public String getZdm_share_type() {
                        return zdm_share_type;
                    }

                    public void setZdm_share_type(String zdm_share_type) {
                        this.zdm_share_type = zdm_share_type;
                    }

                    public String getMd5_url() {
                        return md5_url;
                    }

                    public void setMd5_url(String md5_url) {
                        this.md5_url = md5_url;
                    }
                }
            }

            public static class LittleBannerBean {

                private String id;
                private String img;
                private String vice_title;
                private String is_force_login;
                private String title;
                private String link;
                private String flag;
                private RedirectDataBeanXX redirect_data;
                private String channel;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getVice_title() {
                    return vice_title;
                }

                public void setVice_title(String vice_title) {
                    this.vice_title = vice_title;
                }

                public String getIs_force_login() {
                    return is_force_login;
                }

                public void setIs_force_login(String is_force_login) {
                    this.is_force_login = is_force_login;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getFlag() {
                    return flag;
                }

                public void setFlag(String flag) {
                    this.flag = flag;
                }

                public RedirectDataBeanXX getRedirect_data() {
                    return redirect_data;
                }

                public void setRedirect_data(RedirectDataBeanXX redirect_data) {
                    this.redirect_data = redirect_data;
                }

                public String getChannel() {
                    return channel;
                }

                public void setChannel(String channel) {
                    this.channel = channel;
                }

                public static class RedirectDataBeanXX {

                    private String link;
                    private String link_type;
                    private String sub_type;
                    private String link_val;
                    private String link_title;
                    private String isv_code_second;
                    private String jd_isv_code;
                    private String wiki_id;
                    private String md5_url;

                    public String getLink() {
                        return link;
                    }

                    public void setLink(String link) {
                        this.link = link;
                    }

                    public String getLink_type() {
                        return link_type;
                    }

                    public void setLink_type(String link_type) {
                        this.link_type = link_type;
                    }

                    public String getSub_type() {
                        return sub_type;
                    }

                    public void setSub_type(String sub_type) {
                        this.sub_type = sub_type;
                    }

                    public String getLink_val() {
                        return link_val;
                    }

                    public void setLink_val(String link_val) {
                        this.link_val = link_val;
                    }

                    public String getLink_title() {
                        return link_title;
                    }

                    public void setLink_title(String link_title) {
                        this.link_title = link_title;
                    }

                    public String getIsv_code_second() {
                        return isv_code_second;
                    }

                    public void setIsv_code_second(String isv_code_second) {
                        this.isv_code_second = isv_code_second;
                    }

                    public String getJd_isv_code() {
                        return jd_isv_code;
                    }

                    public void setJd_isv_code(String jd_isv_code) {
                        this.jd_isv_code = jd_isv_code;
                    }

                    public String getWiki_id() {
                        return wiki_id;
                    }

                    public void setWiki_id(String wiki_id) {
                        this.wiki_id = wiki_id;
                    }

                    public String getMd5_url() {
                        return md5_url;
                    }

                    public void setMd5_url(String md5_url) {
                        this.md5_url = md5_url;
                    }
                }
            }
        }

        public static class RowsBean {

            private String article_channel_id;
            private String cell_type;
            private String model_type;
            private String article_channel_name;
            private String article_type_id;
            private String article_type_name;
            private String article_id;
            private String article_url;
            private String article_title;
            private String article_title2;
            private String article_price;
            private String article_subtitle;
            private String article_subtitle_color;
            private String article_date;
            private String article_timesort;
            private String time_sort;
            private String article_format_date;
            private boolean article_anonymous;
            private String article_pic;
            private String article_pic2;
            private String article_worthy;
            private String article_unworthy;
            private String article_favorite;
            private String article_is_sold_out;
            private String article_is_timeout;
            private String article_collection;
            private String article_comment;
            private String article_mall;
            private String article_top;
            private NotInterestBean not_interest;
            private String article_tag;
            private String is_jdz;
            private String is_jsf;
            private String promotion_type;
            private String article_channel_color;
            private String article_district;
            private String pubdate;
            private String stock_status;
            private String article_region;
            private String article_first_channel_name;
            private String article_second_channel_name;
            private String article_third_channel_name;
            private RedirectDataBeanXXX redirect_data;
            private boolean is_highlighted;
            private boolean is_highlighted2;
            private String ga_category;
            private String ga_brand;
            private String ga_log_type;
            private String atp;
            private String tagID;
            private String goods_area;
            private String goods_sold_out;
            private String from_type;
            private String opt_type;
            private String state_type;
            private String is_show_header;
            private String article_superscript;
            private String is_not_interest;
            private String not_interest_title;
            private String new_dislike_style;
            private String pid;
            private String current_page;
            private String article_hash_id;
            private String article_region_title;
            private String probreport_id;
            private String hot_count;
            private ArticleUserBean article_user;
            private String article_filter_content;
            private String article_type;
            private String article_recommend;
            private String article_love_count;
            private String page_timesort;
            private String tag_name;
            private String category_name;
            private String tag_category;
            private String topic_name;
            private String topic_display_name;
            private String title_left_tag;
            private String source_type;
            private String love_rating_count;
            private String article_product_num;
            private List <String> article_tags;
            private List <String> ga_article_tag;
            private List <String> ga_article_category;
            private List <NotInterestNewBean> not_interest_new;

            public String getArticle_channel_id() {
                return article_channel_id;
            }

            public void setArticle_channel_id(String article_channel_id) {
                this.article_channel_id = article_channel_id;
            }

            public String getCell_type() {
                return cell_type;
            }

            public void setCell_type(String cell_type) {
                this.cell_type = cell_type;
            }

            public String getModel_type() {
                return model_type;
            }

            public void setModel_type(String model_type) {
                this.model_type = model_type;
            }

            public String getArticle_channel_name() {
                return article_channel_name;
            }

            public void setArticle_channel_name(String article_channel_name) {
                this.article_channel_name = article_channel_name;
            }

            public String getArticle_type_id() {
                return article_type_id;
            }

            public void setArticle_type_id(String article_type_id) {
                this.article_type_id = article_type_id;
            }

            public String getArticle_type_name() {
                return article_type_name;
            }

            public void setArticle_type_name(String article_type_name) {
                this.article_type_name = article_type_name;
            }

            public String getArticle_id() {
                return article_id;
            }

            public void setArticle_id(String article_id) {
                this.article_id = article_id;
            }

            public String getArticle_url() {
                return article_url;
            }

            public void setArticle_url(String article_url) {
                this.article_url = article_url;
            }

            public String getArticle_title() {
                return article_title;
            }

            public void setArticle_title(String article_title) {
                this.article_title = article_title;
            }

            public String getArticle_title2() {
                return article_title2;
            }

            public void setArticle_title2(String article_title2) {
                this.article_title2 = article_title2;
            }

            public String getArticle_price() {
                return article_price;
            }

            public void setArticle_price(String article_price) {
                this.article_price = article_price;
            }

            public String getArticle_subtitle() {
                return article_subtitle;
            }

            public void setArticle_subtitle(String article_subtitle) {
                this.article_subtitle = article_subtitle;
            }

            public String getArticle_subtitle_color() {
                return article_subtitle_color;
            }

            public void setArticle_subtitle_color(String article_subtitle_color) {
                this.article_subtitle_color = article_subtitle_color;
            }

            public String getArticle_date() {
                return article_date;
            }

            public void setArticle_date(String article_date) {
                this.article_date = article_date;
            }

            public String getArticle_timesort() {
                return article_timesort;
            }

            public void setArticle_timesort(String article_timesort) {
                this.article_timesort = article_timesort;
            }

            public String getTime_sort() {
                return time_sort;
            }

            public void setTime_sort(String time_sort) {
                this.time_sort = time_sort;
            }

            public String getArticle_format_date() {
                return article_format_date;
            }

            public void setArticle_format_date(String article_format_date) {
                this.article_format_date = article_format_date;
            }

            public boolean isArticle_anonymous() {
                return article_anonymous;
            }

            public void setArticle_anonymous(boolean article_anonymous) {
                this.article_anonymous = article_anonymous;
            }

            public String getArticle_pic() {
                return article_pic;
            }

            public void setArticle_pic(String article_pic) {
                this.article_pic = article_pic;
            }

            public String getArticle_pic2() {
                return article_pic2;
            }

            public void setArticle_pic2(String article_pic2) {
                this.article_pic2 = article_pic2;
            }

            public String getArticle_worthy() {
                return article_worthy;
            }

            public void setArticle_worthy(String article_worthy) {
                this.article_worthy = article_worthy;
            }

            public String getArticle_unworthy() {
                return article_unworthy;
            }

            public void setArticle_unworthy(String article_unworthy) {
                this.article_unworthy = article_unworthy;
            }

            public String getArticle_favorite() {
                return article_favorite;
            }

            public void setArticle_favorite(String article_favorite) {
                this.article_favorite = article_favorite;
            }

            public String getArticle_is_sold_out() {
                return article_is_sold_out;
            }

            public void setArticle_is_sold_out(String article_is_sold_out) {
                this.article_is_sold_out = article_is_sold_out;
            }

            public String getArticle_is_timeout() {
                return article_is_timeout;
            }

            public void setArticle_is_timeout(String article_is_timeout) {
                this.article_is_timeout = article_is_timeout;
            }

            public String getArticle_collection() {
                return article_collection;
            }

            public void setArticle_collection(String article_collection) {
                this.article_collection = article_collection;
            }

            public String getArticle_comment() {
                return article_comment;
            }

            public void setArticle_comment(String article_comment) {
                this.article_comment = article_comment;
            }

            public String getArticle_mall() {
                return article_mall;
            }

            public void setArticle_mall(String article_mall) {
                this.article_mall = article_mall;
            }

            public String getArticle_top() {
                return article_top;
            }

            public void setArticle_top(String article_top) {
                this.article_top = article_top;
            }

            public NotInterestBean getNot_interest() {
                return not_interest;
            }

            public void setNot_interest(NotInterestBean not_interest) {
                this.not_interest = not_interest;
            }

            public String getArticle_tag() {
                return article_tag;
            }

            public void setArticle_tag(String article_tag) {
                this.article_tag = article_tag;
            }

            public String getIs_jdz() {
                return is_jdz;
            }

            public void setIs_jdz(String is_jdz) {
                this.is_jdz = is_jdz;
            }

            public String getIs_jsf() {
                return is_jsf;
            }

            public void setIs_jsf(String is_jsf) {
                this.is_jsf = is_jsf;
            }

            public String getPromotion_type() {
                return promotion_type;
            }

            public void setPromotion_type(String promotion_type) {
                this.promotion_type = promotion_type;
            }

            public String getArticle_channel_color() {
                return article_channel_color;
            }

            public void setArticle_channel_color(String article_channel_color) {
                this.article_channel_color = article_channel_color;
            }

            public String getArticle_district() {
                return article_district;
            }

            public void setArticle_district(String article_district) {
                this.article_district = article_district;
            }

            public String getPubdate() {
                return pubdate;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public String getStock_status() {
                return stock_status;
            }

            public void setStock_status(String stock_status) {
                this.stock_status = stock_status;
            }

            public String getArticle_region() {
                return article_region;
            }

            public void setArticle_region(String article_region) {
                this.article_region = article_region;
            }

            public String getArticle_first_channel_name() {
                return article_first_channel_name;
            }

            public void setArticle_first_channel_name(String article_first_channel_name) {
                this.article_first_channel_name = article_first_channel_name;
            }

            public String getArticle_second_channel_name() {
                return article_second_channel_name;
            }

            public void setArticle_second_channel_name(String article_second_channel_name) {
                this.article_second_channel_name = article_second_channel_name;
            }

            public String getArticle_third_channel_name() {
                return article_third_channel_name;
            }

            public void setArticle_third_channel_name(String article_third_channel_name) {
                this.article_third_channel_name = article_third_channel_name;
            }

            public RedirectDataBeanXXX getRedirect_data() {
                return redirect_data;
            }

            public void setRedirect_data(RedirectDataBeanXXX redirect_data) {
                this.redirect_data = redirect_data;
            }

            public boolean isIs_highlighted() {
                return is_highlighted;
            }

            public void setIs_highlighted(boolean is_highlighted) {
                this.is_highlighted = is_highlighted;
            }

            public boolean isIs_highlighted2() {
                return is_highlighted2;
            }

            public void setIs_highlighted2(boolean is_highlighted2) {
                this.is_highlighted2 = is_highlighted2;
            }

            public String getGa_category() {
                return ga_category;
            }

            public void setGa_category(String ga_category) {
                this.ga_category = ga_category;
            }

            public String getGa_brand() {
                return ga_brand;
            }

            public void setGa_brand(String ga_brand) {
                this.ga_brand = ga_brand;
            }

            public String getGa_log_type() {
                return ga_log_type;
            }

            public void setGa_log_type(String ga_log_type) {
                this.ga_log_type = ga_log_type;
            }

            public String getAtp() {
                return atp;
            }

            public void setAtp(String atp) {
                this.atp = atp;
            }

            public String getTagID() {
                return tagID;
            }

            public void setTagID(String tagID) {
                this.tagID = tagID;
            }

            public String getGoods_area() {
                return goods_area;
            }

            public void setGoods_area(String goods_area) {
                this.goods_area = goods_area;
            }

            public String getGoods_sold_out() {
                return goods_sold_out;
            }

            public void setGoods_sold_out(String goods_sold_out) {
                this.goods_sold_out = goods_sold_out;
            }

            public String getFrom_type() {
                return from_type;
            }

            public void setFrom_type(String from_type) {
                this.from_type = from_type;
            }

            public String getOpt_type() {
                return opt_type;
            }

            public void setOpt_type(String opt_type) {
                this.opt_type = opt_type;
            }

            public String getState_type() {
                return state_type;
            }

            public void setState_type(String state_type) {
                this.state_type = state_type;
            }

            public String getIs_show_header() {
                return is_show_header;
            }

            public void setIs_show_header(String is_show_header) {
                this.is_show_header = is_show_header;
            }

            public String getArticle_superscript() {
                return article_superscript;
            }

            public void setArticle_superscript(String article_superscript) {
                this.article_superscript = article_superscript;
            }

            public String getIs_not_interest() {
                return is_not_interest;
            }

            public void setIs_not_interest(String is_not_interest) {
                this.is_not_interest = is_not_interest;
            }

            public String getNot_interest_title() {
                return not_interest_title;
            }

            public void setNot_interest_title(String not_interest_title) {
                this.not_interest_title = not_interest_title;
            }

            public String getNew_dislike_style() {
                return new_dislike_style;
            }

            public void setNew_dislike_style(String new_dislike_style) {
                this.new_dislike_style = new_dislike_style;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getCurrent_page() {
                return current_page;
            }

            public void setCurrent_page(String current_page) {
                this.current_page = current_page;
            }

            public String getArticle_hash_id() {
                return article_hash_id;
            }

            public void setArticle_hash_id(String article_hash_id) {
                this.article_hash_id = article_hash_id;
            }

            public String getArticle_region_title() {
                return article_region_title;
            }

            public void setArticle_region_title(String article_region_title) {
                this.article_region_title = article_region_title;
            }

            public String getProbreport_id() {
                return probreport_id;
            }

            public void setProbreport_id(String probreport_id) {
                this.probreport_id = probreport_id;
            }

            public String getHot_count() {
                return hot_count;
            }

            public void setHot_count(String hot_count) {
                this.hot_count = hot_count;
            }

            public ArticleUserBean getArticle_user() {
                return article_user;
            }

            public void setArticle_user(ArticleUserBean article_user) {
                this.article_user = article_user;
            }

            public String getArticle_filter_content() {
                return article_filter_content;
            }

            public void setArticle_filter_content(String article_filter_content) {
                this.article_filter_content = article_filter_content;
            }

            public String getArticle_type() {
                return article_type;
            }

            public void setArticle_type(String article_type) {
                this.article_type = article_type;
            }

            public String getArticle_recommend() {
                return article_recommend;
            }

            public void setArticle_recommend(String article_recommend) {
                this.article_recommend = article_recommend;
            }

            public String getArticle_love_count() {
                return article_love_count;
            }

            public void setArticle_love_count(String article_love_count) {
                this.article_love_count = article_love_count;
            }

            public String getPage_timesort() {
                return page_timesort;
            }

            public void setPage_timesort(String page_timesort) {
                this.page_timesort = page_timesort;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public String getTag_category() {
                return tag_category;
            }

            public void setTag_category(String tag_category) {
                this.tag_category = tag_category;
            }

            public String getTopic_name() {
                return topic_name;
            }

            public void setTopic_name(String topic_name) {
                this.topic_name = topic_name;
            }

            public String getTopic_display_name() {
                return topic_display_name;
            }

            public void setTopic_display_name(String topic_display_name) {
                this.topic_display_name = topic_display_name;
            }

            public String getTitle_left_tag() {
                return title_left_tag;
            }

            public void setTitle_left_tag(String title_left_tag) {
                this.title_left_tag = title_left_tag;
            }

            public String getSource_type() {
                return source_type;
            }

            public void setSource_type(String source_type) {
                this.source_type = source_type;
            }

            public String getLove_rating_count() {
                return love_rating_count;
            }

            public void setLove_rating_count(String love_rating_count) {
                this.love_rating_count = love_rating_count;
            }

            public String getArticle_product_num() {
                return article_product_num;
            }

            public void setArticle_product_num(String article_product_num) {
                this.article_product_num = article_product_num;
            }

            public List <String> getArticle_tags() {
                return article_tags;
            }

            public void setArticle_tags(List <String> article_tags) {
                this.article_tags = article_tags;
            }

            public List <String> getGa_article_tag() {
                return ga_article_tag;
            }

            public void setGa_article_tag(List <String> ga_article_tag) {
                this.ga_article_tag = ga_article_tag;
            }

            public List <String> getGa_article_category() {
                return ga_article_category;
            }

            public void setGa_article_category(List <String> ga_article_category) {
                this.ga_article_category = ga_article_category;
            }

            public List <NotInterestNewBean> getNot_interest_new() {
                return not_interest_new;
            }

            public void setNot_interest_new(List <NotInterestNewBean> not_interest_new) {
                this.not_interest_new = not_interest_new;
            }

            public static class NotInterestBean {
                private List <String> tag_name;
                private List <String> brand_name;
                private List <String> category_name;

                public List <String> getTag_name() {
                    return tag_name;
                }

                public void setTag_name(List <String> tag_name) {
                    this.tag_name = tag_name;
                }

                public List <String> getBrand_name() {
                    return brand_name;
                }

                public void setBrand_name(List <String> brand_name) {
                    this.brand_name = brand_name;
                }

                public List <String> getCategory_name() {
                    return category_name;
                }

                public void setCategory_name(List <String> category_name) {
                    this.category_name = category_name;
                }
            }

            public static class RedirectDataBeanXXX {

                private String link;
                private String link_type;
                private String sub_type;
                private String link_val;
                private String link_title;
                private String isv_code_second;
                private String jd_isv_code;
                private String wiki_id;
                private String md5_url;

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getLink_type() {
                    return link_type;
                }

                public void setLink_type(String link_type) {
                    this.link_type = link_type;
                }

                public String getSub_type() {
                    return sub_type;
                }

                public void setSub_type(String sub_type) {
                    this.sub_type = sub_type;
                }

                public String getLink_val() {
                    return link_val;
                }

                public void setLink_val(String link_val) {
                    this.link_val = link_val;
                }

                public String getLink_title() {
                    return link_title;
                }

                public void setLink_title(String link_title) {
                    this.link_title = link_title;
                }

                public String getIsv_code_second() {
                    return isv_code_second;
                }

                public void setIsv_code_second(String isv_code_second) {
                    this.isv_code_second = isv_code_second;
                }

                public String getJd_isv_code() {
                    return jd_isv_code;
                }

                public void setJd_isv_code(String jd_isv_code) {
                    this.jd_isv_code = jd_isv_code;
                }

                public String getWiki_id() {
                    return wiki_id;
                }

                public void setWiki_id(String wiki_id) {
                    this.wiki_id = wiki_id;
                }

                public String getMd5_url() {
                    return md5_url;
                }

                public void setMd5_url(String md5_url) {
                    this.md5_url = md5_url;
                }
            }

            public static class ArticleUserBean {

                private String model_type;
                private String article_referrals;
                private String article_avatar;
                private String article_user_smzdm_id;
                private AuthorRoleBean author_role;

                public String getModel_type() {
                    return model_type;
                }

                public void setModel_type(String model_type) {
                    this.model_type = model_type;
                }

                public String getArticle_referrals() {
                    return article_referrals;
                }

                public void setArticle_referrals(String article_referrals) {
                    this.article_referrals = article_referrals;
                }

                public String getArticle_avatar() {
                    return article_avatar;
                }

                public void setArticle_avatar(String article_avatar) {
                    this.article_avatar = article_avatar;
                }

                public String getArticle_user_smzdm_id() {
                    return article_user_smzdm_id;
                }

                public void setArticle_user_smzdm_id(String article_user_smzdm_id) {
                    this.article_user_smzdm_id = article_user_smzdm_id;
                }

                public AuthorRoleBean getAuthor_role() {
                    return author_role;
                }

                public void setAuthor_role(AuthorRoleBean author_role) {
                    this.author_role = author_role;
                }

                public static class AuthorRoleBean {

                    private String official_auth_icon;
                    private String official_auth_desc;

                    public String getOfficial_auth_icon() {
                        return official_auth_icon;
                    }

                    public void setOfficial_auth_icon(String official_auth_icon) {
                        this.official_auth_icon = official_auth_icon;
                    }

                    public String getOfficial_auth_desc() {
                        return official_auth_desc;
                    }

                    public void setOfficial_auth_desc(String official_auth_desc) {
                        this.official_auth_desc = official_auth_desc;
                    }
                }
            }

            public static class NotInterestNewBean {

                private String name;
                private String type;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
    }
}
