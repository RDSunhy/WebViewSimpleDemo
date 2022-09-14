package com.sunhy.demo.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.reflect.TypeToken
import com.sunhy.demo.R
import com.sunhy.demo.base.entity.BaseEntity
import com.sunhy.demo.base.entity.News
import com.sunhy.demo.base.http.HttpUtils
import com.sunhy.demo.databinding.ActivityNewsListBinding
import com.sunhy.demo.databinding.ItemNewsBinding
import com.sunhy.demo.web.activity.NewsDetailActivity
import kotlinx.coroutines.*

class NewsListActivity : AppCompatActivity() {

    protected lateinit var mBinding: ActivityNewsListBinding
    private val mAdapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_list)
        mBinding.lifecycleOwner = this

        mBinding.rvNews.layoutManager = LinearLayoutManager(this)
        mBinding.rvNews.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mBinding.rvNews.adapter = mAdapter

        mBinding.rvNews.visibility = View.GONE
        mBinding.loadingView.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
//            val url = "https://api.yparse.com/api/news?ac=detail&ids=96962,96963,96964,96965,96966,96967,96968,96969,96970,96971,96971,96973,96974,96975,96976,96977"
//            val result = HttpUtils.apiService.getNewsList(url)
            // 模拟请求
            delay(2000)
            val result = GsonUtils.fromJson<BaseEntity<News>>(dataJson, object : TypeToken<BaseEntity<News>>(){}.type)
            withContext(Dispatchers.Main) {
                mBinding.rvNews.visibility = View.VISIBLE
                mBinding.loadingView.visibility = View.GONE
                mAdapter.setNewsData(result.list)
            }
        }
    }

    inner class NewsAdapter : RecyclerView.Adapter<BaseBindingViewHolder<ItemNewsBinding>>() {

        private val mDatas: MutableList<News> = mutableListOf()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BaseBindingViewHolder<ItemNewsBinding> {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val binding: ItemNewsBinding =
                DataBindingUtil.inflate(inflater, R.layout.item_news, parent, false)
            return BaseBindingViewHolder(binding.root)
        }

        override fun onBindViewHolder(
            holder: BaseBindingViewHolder<ItemNewsBinding>,
            position: Int
        ) {
            val binding = holder.dataBinding ?: return
            val item = mDatas[position]
            binding.tvTitle.text = item.art_name
            binding.tvType.text = item.type_name
            binding.tvAuthor.text = item.art_author

            holder.itemView.setOnClickListener {
                val intent = Intent(this@NewsListActivity, NewsDetailActivity::class.java)
                intent.putExtra("title", item.art_name)
                intent.putExtra("tag", item.type_name)
                intent.putExtra("content", item.art_content)
                startActivity(intent)
            }
        }

        override fun getItemCount() = mDatas.size

        fun setNewsData(data: List<News>) {
            mDatas.clear()
            mDatas.addAll(data)
            notifyDataSetChanged()
        }

    }

    inner class BaseBindingViewHolder<DB : ViewDataBinding>(view: View) :
        RecyclerView.ViewHolder(view) {
        val dataBinding = DataBindingUtil.bind<DB>(view)
    }

    private val dataJson = """
        {"code":1,"msg":200,"page":1,"pagecount":1,"limit":"20","total":15,"list":[{"art_id":96977,"type_id":45,"type_id_1":2,"art_name":"《闻香榭》大结局 袁梓铭运动装帅气亮相马拉松活动","art_status":1,"art_letter":"W","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/98FQ2m8vo000ozGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7W8931BxE5QzBI0wO1b63GlMcSib4f3SgCLuBhiQAshZ90mtAO0O0OO0O0O.jpg","art_blurb":"玄幻浪漫古装剧《闻献血》本周迎来大收官,大师兄元稹&ldquo;幕后大boss&rdquo;身份揭露之后与众人迎来巅峰对决。饰演帅气邪魅大师兄的袁梓铭本周一身运动装扮与刘畊宏、袁姗姗等众明星一起亮相某","art_up":1257,"art_hits":1257,"art_hits_day":2333,"art_hits_month":3070,"art_time":"2022-09-13 15:00:36","art_score":"7.0","art_content":"<p>玄幻浪漫古装剧《闻献血》本周迎来大收官,大师兄元稹&ldquo;幕后大boss&rdquo;身份揭露之后与众人迎来巅峰对决。饰演帅气邪魅大师兄的袁梓铭本周一身运动装扮与刘畊宏、袁姗姗等众明星一起亮相某马拉松跑步活动、大长腿和帅气脸庞吸睛。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/98FQ2m8vo000ozGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7W8931BxE5QzBI0wO1b63GlMcSib4f3SgCLuBhiQAshZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/psUF3jNoo00ooo00o2Cfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000ofuonwUkB4Dx0sykr4GuifybsOvaNb7TgrZvBllQQlxZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8pNT3WgsqWGfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7Hl8XISkh1Rxhdllo000o4N6HD3O8D1atb7Rwrcv0llRF4iZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/p8UAhzt7oo00omKfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000o68o3wZw0tTkhFinO5a6XXxP5DyNYb4R1bf7U9hQQFyZ90mtAO0O0OO0O0O.jpg\"><\/p>","type_name":"影视资讯"},{"art_id":96976,"type_id":45,"type_id_1":2,"art_name":"倪言新剧《从前慢白首要相离》向老戏骨学习","art_status":1,"art_letter":"N","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/9ZIFjzJ4qzCfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7HspSVEwk8CkRBhwL8L732kbsKgaNX9TlTeuRkzQFwjZ90mtAO0O0OO0O0O.jpg","art_blurb":"倪言新剧《从前慢，白首要相离》上线了！剧中倪言饰演婚礼策划师黎苏苏，为爷爷奶奶举办 50周年金婚典礼，没想到典礼上奶奶却当场提出离婚！更郁闷的是黎苏苏向相恋多年的男友求婚也惨遭婉拒，因为男友根本不相信","art_up":1532,"art_hits":1532,"art_hits_day":1055,"art_hits_month":2998,"art_time":"2022-09-13 15:00:39","art_score":"3.0","art_content":"<img src=\"https:\/\/img.yparse.com\/upload\/9ZIFjzJ4qzCfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7HspSVEwk8CkRBhwL8L732kbsKgaNX9TlTeuRkzQFwjZ90mtAO0O0OO0O0O.jpg\"><p>倪言新剧《从前慢，白首要相离》上线了！剧中倪言饰演婚礼策划师黎苏苏，为爷爷奶奶举办 50周年金婚典礼，没想到典礼上奶奶却当场提出离婚！更郁闷的是黎苏苏向相恋多年的男友求婚也惨遭婉拒，因为男友根本不相信婚姻，原本对爱情充满向往的黎苏苏，先后经历爷爷奶奶的离婚事件和求婚被拒，对自己的信念产生了深深的怀疑。<\/p><p>《从前慢白首要相离》视角独特，关注了父辈祖辈的情感世界对青年一代的影响，在众多仙侠，偶像剧泛滥的大环境下可谓匠心独具，温情而又真实，诚意满满。众多实力派老戏骨的加盟，也让这部戏的质感得到升华。而与爷爷奶奶的对手戏更是让倪言直呼收获满满！在跟资深老戏骨和人艺老师们的合作中，倪言一定又学到了很多的干货吧。<\/p><p>博观而约取，厚积而薄发，倪言一直在脚踏实地积累作品，未来还有《那年时光安好》《你是我的美味》等多部剧集待播！期待倪言的新作品能给我们带来新的惊喜！<\/p>","type_name":"影视资讯"},{"art_id":96975,"type_id":45,"type_id_1":2,"art_name":"《外星女生柴小七2》定档 徐志贤外冷内热霸总升级","art_status":1,"art_letter":"W","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/oJZUiW14qzKfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7HtoCcSlUIHxxBvkekJ73WkaZCmO4GoHFDYvRwxEQpwZ90mtAO0O0OO0O0O.jpg","art_blurb":"近日，由徐志贤、万鹏领衔主演的浪漫甜宠剧《外星女生柴小七2》官宣于9月16日开播。该剧讲述了方冷与柴小七的爱情即将圆满却又再生波折的故事，由徐志贤饰演方冷依旧霸气十足，在官方释出的预告片中更是上演浴室","art_up":4371,"art_hits":4371,"art_hits_day":2491,"art_hits_month":4167,"art_time":"2022-09-13 15:00:42","art_score":"7.0","art_content":"<p>近日，由徐志贤、万鹏领衔主演的浪漫甜宠剧《外星女生柴小七2》官宣于9月16日开播。该剧讲述了方冷与柴小七的爱情即将圆满却又再生波折的故事，由徐志贤饰演方冷依旧霸气十足，在官方释出的预告片中更是上演浴室沐浴戏码，好身材显露无疑。徐志贤健硕的身材以及温柔霸道的气质，令观众大呼&ldquo;还是熟悉的配方&rdquo;，期待值倍增。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/oJZUiW14qzKfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7HtoCcSlUIHxxBvkekJ73WkaZCmO4GoHFDYvRwxEQpwZ90mtAO0O0OO0O0O.jpg\"><\/p><p>徐志贤大秀半裸身材 身材管理获赞<\/p><p>据悉，在电视剧《外星女生柴小七2》中，方冷的记忆力随着柴小七被母星强行带走后中断消失，&ldquo;冷气&rdquo;夫妇能否冲破阻碍，寻回旧时甜蜜的谜题也将在剧中逐一揭晓。 由徐志贤饰演的方冷对待事业尽职负责，对待爱情亦是深情温柔；除此之外，徐志贤更是在剧中为观众&ldquo;贡献&rdquo;荷尔蒙爆棚的浴室戏份，在水流的冲击中尽显健硕身材，绝佳的身材管理和状态都获得观众的及粉丝的一致认可；在花絮中化身举铁达人，原地扛起女主的爆发力也引起众人赞叹。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/9JJWiDIrqWSfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7G4oHwRwk0FxkVvwuwPuyfxbpCjO4b5TAbbu0hhQVp3Z90mtAO0O0OO0O0O.jpg\"><\/p><p>霸气细致反转魅力 徐志贤新剧开启甜宠模式<\/p><p>自《外星女生柴小七》第一季开播以来，由优质实力派演员徐志贤饰演的霸道总裁方冷随即撩动万千少女心，收获不凡的热度与口碑。在即将开播的《外星女生柴小七2》中，方冷延续了第一部的反差性格，霸道高冷且温柔细腻，面临女友被&ldquo;洗脑&rdquo;丧失记忆等一系列问题，在失忆梗中徐志贤将通过怎样的精彩演绎，令观众在笑中带泪之余还能感受到新鲜与惊喜感，让人拭目以待。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/oclR2jguoo00oWCfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7G8rXVClkxVxhcyx7EO7HOlMZGmPtysSgqD6hlqQg4kZ90mtAO0O0OO0O0O.jpg\"><\/p><p>从《如果，爱》中英俊幽默的陆阳，到《萌妻食神》中的世子爷夏淳于，再到《外星女生柴小七》中魅力十足的方冷，演员徐志贤用精湛的演技塑造了一个又一个令观众印象深刻的角色。《柴小七2》将于9月16日开播，由徐志贤演唱的主题曲《destiny lover》MV也于今日上线，磁性厚重的声线自带故事感，演、唱俱佳令人倍感惊喜。<\/p>","type_name":"影视资讯"},{"art_id":96974,"type_id":47,"type_id_1":2,"art_name":"2022中国乡村振兴文化节影响力盛典及中国乡村产业振兴发展论坛在昆明盛大召开","art_status":1,"art_letter":"2","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/oJIFjG0qq2ufoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oo000o5onEZl0tVw0FvlbxdunGgapP1PYb4TguL7BhmFF4mZ90mtAO0O0OO0O0O.jpg","art_blurb":"2022中国乡村振兴文化节影响力盛典及中国乡村产业振兴发展论坛在昆明盛大召开！2022年8月21日，在中国小康建设研究会乡村产业工作委员会的指导下，一场盛大的乡村振兴主题的中国乡村振兴文化节影响力盛典","art_up":4224,"art_hits":4224,"art_hits_day":4115,"art_hits_month":4846,"art_time":"2022-09-13 15:00:43","art_score":"5.0","art_content":"<p ><img src=\"https:\/\/img.yparse.com\/upload\/oJIFjG0qq2ufoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oo000o5onEZl0tVw0FvlbxdunGgapP1PYb4TguL7BhmFF4mZ90mtAO0O0OO0O0O.jpg\"><\/p><p>2022中国乡村振兴文化节影响力盛典及中国乡村产业振兴发展论坛在昆明盛大召开！<\/p><p>2022年8月21日，在中国小康建设研究会乡村产业工作委员会的指导下，一场盛大的乡村振兴主题的中国乡村振兴文化节影响力盛典及中国乡村产业振兴发展论坛在美丽的七彩云南&middot;昆明云安会都国际会议中心隆重召开，来自部委的相关领导和全国多个地区的乡村振兴领导、文旅部门领导、乡村书记、乡村产业企业家及乡村文化、乡村非遗代表人物，以及歌手<\/p><p>吴易航、主持人解唯一、美术师齐敬生、书画家干文卿，歌手许赋、歌手张叶子、歌手邓宸、歌手诗韵雅歌、武汉籍歌手贺七七、原创音乐人罗智鸿、等出席2022中国乡村振兴文化节影响力盛典及中国乡村产业振兴发展论坛。共同为乡村振兴助力，开启奔向共同富裕的航程。<\/p><p>乡村振兴战略是中国特色社会主义进入新时代后的一项重大战略部署，是新时代中国特色社会主义思想的重要组成部分，是事关国家发展的全局性、历史性任务。领导人强调，乡村振兴，关键是产业要振兴，产业是发展的根基，产业兴旺，乡亲们收入才能稳定增长。本次论坛围绕&ldquo;中国乡村产业振兴发展&rdquo;为主题，以推动中国乡村产业转型升级、融合创新、共同发展，推动共同富裕为宗旨，搭建一个政府、学者、企业、乡村一线工作者交流、对话的平台。<\/p><p>乡村振兴五大振兴政策中，文化振兴是推动产业振兴的首要工具之一，在乡村振兴的相关文件指出：大力挖掘乡村传统文化，传承乡村非遗，在此次论坛的乡村文化环节当中，来自全国二十几个地区的艺术团展示了不同风格的地方文化艺术节目，他们用不同的艺术形式展示了各自的家乡美、表达了对家乡的热爱，主办方为了挖掘了推广更多的乡村文化、助力乡村文旅、带动乡村产业给优秀的节目进行了颁奖，鼓励他们继续发扬乡村优秀传统文化。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/rsIE3Tslo000oTKfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7O5oiYZmRoFzEIwlbsK6XOkaZT3Od3oo00oHFGI6U43EgokZ90mtAO0O0OO0O0O.jpg\"><\/p><p>此次，歌手吴易航 获得2022中国乡村振兴文化节影响力人物奖。<\/p><p>乡村产业振兴是五大振兴政策的重中之重，面对当下的新政策，各领域对产业振兴的新方向还尚未真正的解读透彻，很难在全新的政策方向里面来开展内容落地，组委会专门邀请了相关领导、学术专家和奋战在一线的基层干部围绕产业如何升级、如何转型、如何发展等问题进行了解读和探讨，地方第一书记代表一并分享了他们在乡村振兴实施的相关经验。<\/p><p>乡村振兴当中五大振兴，用乡村文化赋能、用乡村人才效力、用乡村生态引流、用乡村组织引导，全力助力乡村产业发展。此次文化节及产业论坛得到了众多乡村产业企业和乡村绿色行业协会、平台的大力支持，同时组委会乡村振兴帮扶服务平台&ldquo;乡互帮&rdquo;盛大启动，共同联动合作落地地方乡村帮扶服务中心，在接下来的全国乡村帮扶项目中强强联手，共同为所需要助力的乡村效力，为国家推动共同富裕的道路上增沙添石。此次<\/p><p>2022中国乡村振兴文化节影响力盛典及中国乡村产业振兴发展论坛圆满成功，获得好评。<\/p>","type_name":"娱乐八卦"},{"art_id":96973,"type_id":47,"type_id_1":2,"art_name":"《闻香榭》正式收官 刘贾玺发纯白写真告别“兰泽”","art_status":1,"art_letter":"W","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/ppNYi2h5qWqfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oK5o3YTwUpWlxUylb8L5n31bMPwOdX7SlGDuxJgEQ11Z90mtAO0O0OO0O0O.jpg","art_blurb":"古偶剧《闻香榭》上周迎来正式收官,一直女扮男装的&ldquo;兰泽&rdquo;女装亮相惊艳,&ldquo;坦诚相见&rdquo;夫妇也终于挑破窗户纸有情人终成眷属。刘贾玺社交账号发布一组纯白色写真配","art_up":3539,"art_hits":3539,"art_hits_day":788,"art_hits_month":3872,"art_time":"2022-09-13 15:00:44","art_score":"3.0","art_content":"<p>古偶剧《闻香榭》上周迎来正式收官,一直女扮男装的&ldquo;兰泽&rdquo;女装亮相惊艳,&ldquo;坦诚相见&rdquo;夫妇也终于挑破窗户纸有情人终成眷属。刘贾玺社交账号发布一组纯白色写真配文&ldquo;酷女孩就是我,我就是酷女孩&rdquo;告别兰泽,粉丝纷纷留言点赞这组写真的干净清爽,称其为&ldquo;白开水&rdquo;气质女孩,笑容纯净,不染尘埃。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/ppNYi2h5qWqfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oK5o3YTwUpWlxUylb8L5n31bMPwOdX7SlGDuxJgEQ11Z90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/9MdW2W94rmWfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7XspH0VwkhVkktjlulZvCOrOZCkOtb3TAPcth5kFFkgZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/oJQAj2h5qzafoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7S793cYkBgElUFjxrEP7CDxOsf0bdX9H1bfvB82TgsjZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pcVQ2Wp8rGufoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7Hv8XZFxEoCl0s1letbvCHxPJegPtz7SQOPvhgzFg12Z90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pclYjm4qoo00oWCfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000ofo93MTmB9QkRBklroKvST3MZahPdP3HAODvEhjRFskZ90mtAO0O0OO0O0O.jpg\"><\/p>","type_name":"娱乐八卦"},{"art_id":96971,"type_id":47,"type_id_1":2,"art_name":"多面黎一萱中秋新造型 出尘精灵降人间","art_status":1,"art_letter":"D","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/9JZY3GokqTKfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7Tq8HASkkMFwUc0xusP5iSrMcLwOd2rHADYv01qEQogZ90mtAO0O0OO0O0O.jpg","art_blurb":"时值中秋佳节,多面大青衣演员黎一萱曝光了一组新造型,尖尖的耳朵,洁白的翅膀,犹如来到人间玩耍的精灵仙子,在水波的衬映下,舞动指尖的鲜花,文艺清新感简直拉满。而这组造型也如同将月亮的仙境与人间的仙境连接","art_up":487,"art_hits":487,"art_hits_day":2526,"art_hits_month":4570,"art_time":"2022-09-13 15:00:48","art_score":"7.0","art_content":"<p>时值中秋佳节,多面大青衣演员黎一萱曝光了一组新造型,尖尖的耳朵,洁白的翅膀,犹如来到人间玩耍的精灵仙子,在水波的衬映下,舞动指尖的鲜花,文艺清新感简直拉满。而这组造型也如同将月亮的仙境与人间的仙境连接在一起,对中秋的氛围营造新鲜感让人赏心悦目。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/9JZY3GokqTKfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7Tq8HASkkMFwUc0xusP5iSrMcLwOd2rHADYv01qEQogZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8MNR3W8srzGfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7bp83MUxUtUwEtkwbkH53LwaZWjOdPoo00oRlOOux0xEV0nZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8MNUhjx4o000omefoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7Tu8SEQl05XlRUywL8OunKmP5amP4eoSAqKththTgwmZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/ochVjD4vr2efoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7a8oSBFxkNQlktknOla7neqMJWvNYStH1OO7BhmFQl1Z90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8JUD2zx7oo00oGefoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7PlrXcYwU4Aw0NknbgHvCHwP8ClNd36HFPbvBg0QQglZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/p8gCj2h7o000oTafoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oo000o4oyFCk0xQxEBhxrFZ6Xyha5ShNdT3HwWM7BhhQAhyZ90mtAO0O0OO0O0O.jpg\"><\/p>","type_name":"娱乐八卦"},{"art_id":96970,"type_id":45,"type_id_1":2,"art_name":"小童星周娅奚参加《大美雄州·妙游中秋》主题节目","art_status":1,"art_letter":"X","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/95JUh2opo000ozCfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7LqoCdCkkpUlkdmk7EI5negbcWubdX6HQPe7U1iEgB2Z8c4tAO0O0OO0O0O.jpg","art_blurb":"又是一轮金黄圆月，挂在墨染天空之上，遥相呼应人间团圆灯火，无论天南海北，相聚离别千言万语凝聚思念的声音，真情相伴，天涯共此时！2022年9月10日，《大美雄州&middot;妙游中秋》主题节目在雄县广","art_up":188,"art_hits":188,"art_hits_day":4063,"art_hits_month":1177,"art_time":"2022-09-13 15:00:51","art_score":"1.0","art_content":"<p>又是一轮金黄圆月，挂在墨染天空之上，遥相呼应人间团圆灯火，无论天南海北，相聚离别千言万语凝聚思念的声音，真情相伴，天涯共此时！<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/95JUh2opo000ozCfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7LqoCdCkkpUlkdmk7EI5negbcWubdX6HQPe7U1iEgB2Z8c4tAO0O0OO0O0O.jpg\"><\/p><p>2022年9月10日，《大美雄州&middot;妙游中秋》主题节目在雄县广播电视台隆重举行，在这个美好的季节，举杯向月，星光相聚，这是一个歌舞的盛会，这是一场颇具震撼力的视听盛宴！<\/p><p>歌唱大美雄州，赞美大美雄州！为迎接中秋佳节的到来，经工作人员精心布置的演播大厅，像是一幅幅美丽的画卷，场面恢宏壮观，舞美精致细腻，舞台上，美轮美奂的灯光下，演员们载歌载舞，乐器表演、诗朗诵、武术、歌曲等，一个个精心准备的节目陆续登台亮相，台上激情四射，台下掌声雷动。随着歌舞节目的一一呈现，美丽雄州的山山水水，人物风情均浮现在现场观众的眼前。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8JUHjGovqWKfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oftoCdElB4DxBJvxrsH6nakbZWiOYD8SQbc7RhjRQwjZ8c4tAO0O0OO0O0O.jpg\"><\/p><p>此次节目，小童星周娅奚再次获邀登台演唱歌曲《人间》，她的歌声婉转清亮，像歌唱春天的黄莺，她的舞台掌控力令人啧啧称奇，稳重而又不失俏皮，她的形象甜美清丽，小小年纪就是舞台常客。《人间》这首歌曲的辗转启承，每个音符自周娅奚唱出来既具童声的稚嫩美好，又有对未来的无限期盼和美好祝福。<\/p><p>小小年纪的周娅奚，已经是雄安电视台的&ldquo;常客&rdquo;了，早 在2020年2月，周娅奚就曾在《雄安少儿春晚》节目中独唱《最美的光》，在2021年8月中央广播电视总台《心连心走进雄安》节目中演唱歌曲《声声慢》。<\/p><p>最后，在小演员刘乐民、赵景涵、管思诺、高美誉、于诣轩表演的武术压轴节目《 盛世雄风》中，《大美雄州&middot;妙游中秋》主题节目落下帷幕。<\/p>","type_name":"影视资讯"},{"art_id":96969,"type_id":47,"type_id_1":2,"art_name":"葛天新戏造型曝光，一袭红衣古装，美成经典","art_status":1,"art_letter":"G","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/r5ZUjD15r2afoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oo000o89ydDlkIDw0NmlbxZ6nOiPJP0NIb3GwSP7kgzRAx3Z90mtAO0O0OO0O0O.jpg","art_blurb":"众所周知，作为演艺圈&ldquo;大青衣&rdquo;的葛天，只要新戏一开拍，就备受关注。作为一部古装剧，这部作品，从造型到人物设定，都令人颇为期待。在之前的电影《法医宋慈2之四宗罪》中，葛天饰演美艳","art_up":3474,"art_hits":3474,"art_hits_day":3113,"art_hits_month":1388,"art_time":"2022-09-13 15:00:53","art_score":"4.0","art_content":"<p ><img src=\"https:\/\/img.yparse.com\/upload\/r5ZUjD15r2afoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oo000o89ydDlkIDw0NmlbxZ6nOiPJP0NIb3GwSP7kgzRAx3Z90mtAO0O0OO0O0O.jpg\"><\/p><p>众所周知，作为演艺圈&ldquo;大青衣&rdquo;的葛天，只要新戏一开拍，就备受关注。作为一部古装剧，这部作品，从造型到人物设定，都令人颇为期待。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/o8IFjjt4o000oDGfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oPv8SZGlElTzEYznLsG63Hwa5D0OdL9TQqLv09gElsmZ90mtAO0O0OO0O0O.jpg\"><\/p><p>在之前的电影《法医宋慈2之四宗罪》中，葛天饰演美艳动人且武艺高强的江湖女侠丹青，成功地塑造了一位&ldquo;护夫狂魔&rdquo;的形象。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/ppFVizMloo00oGefoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oO4piUZlU5Qwkdlwr0LuCH3OZoo00owbt3oo00oTwTY7hpjQ113Z90mtAO0O0OO0O0O.jpg\"><\/p><p>在新曝光的剧照中，葛天饰演的角色，依旧穿着一身古装，造型惊艳，再加上葛天白皙的肌肤，精致的容颜，让人怦然心动。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/oMICiz0ro000oGKfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oTv8yBBmBoDlUBhxrxc6HakbJSmPtz9TgXc7hwzEVsnZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pcMAjz8kqzafoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000ooo00oprXZGlRpVxkVglOxd6yerbJalPtL5SwTZuE9mFAl2Z90mtAO0O0OO0O0O.jpg\"><\/p><p>都说美人在骨不在皮，但葛天，最吸引人的，却是她与众不同的气质，无论是身穿古装，还是现代服装，都隐藏不了葛天身上的灵气，只要是葛天在场的地方，都会吸引众人的目光。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/r8lRiDwlo000oDafoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7Ht9H0VxklTxRJnkrgI63OjOpbyb4f8G1TYuU8xFQ9wZ90mtAO0O0OO0O0O.jpg\"><\/p><p>毕业于中央戏剧学院表演系的葛天，自进入演艺圈之后，就一直认真努力地在拍戏，每年都会为观众，奉献出优质的作品，其高产优质的印象，深入人心。相信葛天又会给大家带来一部诚意之作，敬请期待吧！<\/p>","type_name":"娱乐八卦"},{"art_id":96968,"type_id":47,"type_id_1":2,"art_name":"《开新炙造夜》引领文化创新，聚焦青年价值引领","art_status":1,"art_letter":"K","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/p8YHimho000ooo00oGGfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oPqrHRCxRpTkUAwkb1eu3CkasL1O9D3G1eC6U1mQF4nZ90mtAO0O0OO0O0O.jpg","art_blurb":"由央视网全新打造的青春文化直播综艺《开新炙造夜》，9月9日19:30在央视网、央视影音和央视网新媒体矩阵同步直播。直播累计观看量达682.8万，热搜28个，相关话题阅读量达1.72亿。《开新炙造夜》以","art_up":2374,"art_hits":2374,"art_hits_day":656,"art_hits_month":466,"art_time":"2022-09-13 15:00:59","art_score":"8.0","art_content":"<p>由央视网全新打造的青春文化直播综艺《开新炙造夜》，9月9日19:30在央视网、央视影音和央视网新媒体矩阵同步直播。直播累计观看量达682.8万，热搜28个，相关话题阅读量达1.72亿。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/p8YHimho000ooo00oGGfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oPqrHRCxRpTkUAwkb1eu3CkasL1O9D3G1eC6U1mQF4nZ90mtAO0O0OO0O0O.jpg\"><\/p><p>《开新炙造夜》以&ldquo;开新之炙，创新呈现&rdquo;为制作理念，首期节目以&ldquo;穿&lsquo;月&rsquo;而来&rdquo;为主题，以古今文化交流碰撞的形式，展现新潮玩法传递传统文化，为传统文化注入活力!节目收获好评无数。<\/p><p>文化交流出新招 两代偶像中秋记忆引发&rdquo;回忆杀&ldquo;<\/p><p>《开新炙造夜》汇聚了中央广播电视总台主持人尼格买提，全民实力唱将张信哲，青年偶像郑乃馨、INTO1伯远、INTO1尹浩宇，民谣乐队组合好妹妹乐队及短视频达人疯狂小杨哥兄弟等。《开新炙造夜》在阵容上出新招，两代偶像激情碰撞、各圈层达人破壁交流，他们在节目中各自分享自己对于中秋的独特记忆，引发现场&ldquo;回忆杀&rdquo;，古今花式过节方式，让人惊呼古代人也太会玩了!<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/ppNXhz0qo000o2qfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000o7sp3RElENVkhFvkbBa6if1bsekNdz7HATcu09jRQ53Z90mtAO0O0OO0O0O.jpg\"><\/p><p>在游戏环节，嘉宾之间的互动更是产生了奇妙化学反应，笑点频出!开场小杨哥霹雳舞惹众人瞩目，节目里还体验了古人的中秋传统活动，投壶游戏INTO1伯远零命中，引发笑料无数，飞花令等传统游戏融入潮流玩法，让郑乃馨和INTO1尹浩宇对于中国传统文化的浓厚兴趣，&ldquo;圆&rdquo;曲接龙也是精彩纷呈!<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pMgDij14o000o2Ofoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oo000o7rHARkk4AxxVnwb4HunGna5b0adb9SAGKvUpqRwogZ90mtAO0O0OO0O0O.jpg\"><\/p><p>音乐环节，开场秀串烧舞台古风现代激情碰撞，让观众重温古今经典曲目，引发观众连连称赞。张信哲经典曲目《爱就一个字》引发全场大合唱，评论区观众皆呼&ldquo;爷青回!&rdquo;，好妹妹的现场演唱稳如CD，三位年轻歌手为整场直播增添了青春色彩，总台央视主持人尼格买提的惊喜献唱将节目推上观看小高潮。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8sgA2Tst8zefoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000o7koiJBlR9exREywukN6CeqOsWmNIboo00oTlGDvUljQA13Z90mtAO0O0OO0O0O.jpg\"><\/p><p>以开新之姿，为文化传承赋新<\/p><p>《开新炙造夜》从传统文化中不断探索新玩法，让潮流融入传统文化之中，尽展中国传统文化博大精深之美。<\/p><p>节目主题穿&ldquo;月&rdquo;而来，一语双关，既贴合传统节日中秋节，又表现节目形式&mdash;&mdash;四位嘉宾从古代&ldquo;穿越&rdquo;而来，与现代好友团聚。从古代穿&rdquo;月&ldquo;而来的四大才子介绍了古人过中秋的方式，吟诗作对、对月当歌、把酒言欢，中国传统的文化和习俗让来自泰国的郑乃馨和INTO1尹浩宇直呼浪漫!<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/r8JTi215oo00ozGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oXk93MYwRoHzUQzlOxZu3P2a8PzOoGvRgfe7RpnQggsZ90mtAO0O0OO0O0O.jpg\"><\/p><p>在节目环节设计上，也随处可见&ldquo;开新&rdquo;态度。节目开场曲古风现代结合产生奇妙效果，古代宴饮盛行游戏&ldquo;投壶&rdquo;与现代运动社交新方式&ldquo;花式足球&rdquo;激情碰撞，展现古今双重魅力，飞花令与猜灯谜等经典游戏比拼引出新潮月饼盲盒惩罚，现场妙趣横生。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/88FSjjJ58zGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000ooo00ospXNDk0MAwkBllekOunKlbJPyaN37TQbcuEhgR1lwZ90mtAO0O0OO0O0O.jpg\"><\/p><p>注重青年力量 ，聚焦青年精神塑造<\/p><p>《开新炙造夜》从青年视角出发，以开新的表达，探索综艺新玩法，诠释传统文化的弦歌不辍、兼容并蓄。以开新的文化，探索传统文化新表达。同时《开新炙造夜》也以传统文化为根基，从形式到主题，从文化传播到价值引领，都坚持着传统文化创新，多角度、多方面提升青年对传统文化的自豪感，从而带动青年主动将新潮文化融入传统文化之中。<\/p><p>以炙热为起点前往开新的起点，创造延续开新的终点，再用开新描绘九州底蕴。《开新炙造夜》的热播，让我们对传统文化的创新勃兴充满期待!<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/88UF2jwuqWOfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oDspX0QxklRxkY1lo000oxa6XP2bMWmbtX9S1ONuhg3TghxZ90mtAO0O0OO0O0O.jpg\"><\/p>","type_name":"娱乐八卦"},{"art_id":96967,"type_id":47,"type_id_1":2,"art_name":"巡览秋收壮丽，链接县域发展，CCTV-17启动“县里丰收”媒体行动","art_status":1,"art_letter":"X","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/8MMAjm1oo00ormufoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000o7v93MQl0JWwREwkOoK7yClap7zNYb4TVaC6hNrRFtwZ90mtAO0O0OO0O0O.jpg","art_blurb":"金秋九月，华夏大地累累硕果，处处美景。全国从南到北、自西向东的主要农作物和经济作物产区、主要牧区、特色水果产区陆续进入秋收季节。在第五个中国农民丰收节即将到来之际，中央广播电视总台农业农村节目中心精心","art_up":4549,"art_hits":4549,"art_hits_day":3269,"art_hits_month":1554,"art_time":"2022-09-13 15:01:06","art_score":"3.0","art_content":"<p>金秋九月，华夏大地累累硕果，处处美景。全国从南到北、自西向东的主要农作物和经济作物产区、主要牧区、特色水果产区陆续进入秋收季节。<\/p><p>在第五个中国农民丰收节即将到来之际，中央广播电视总台农业农村节目中心精心策划推出&ldquo;丰收中国&rdquo;融合传播行动。9月14日，&ldquo;丰收中国&rdquo;系列产品之&ldquo;县里丰收&rdquo; 融合传播活动将以来自县域的收获景象致敬劳动、礼赞秋收，拉开2022丰收季报道的帷幕。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8MMAjm1oo00ormufoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000o7v93MQl0JWwREwkOoK7yClap7zNYb4TVaC6hNrRFtwZ90mtAO0O0OO0O0O.jpg\"><\/p><p>&ldquo;丰收中国之县里丰收&rdquo;融合传播活动通过采自一线的视频、图片等聚焦全国各县的秋收场景、秋收好物，以大小屏联动的形式多维度展示各地乡村振兴战略的实施成果和&ldquo;三农&rdquo;发展新面貌，展现最真实、最质朴、最火热的劳动场景，为党的二十大献礼。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pJRZjG4rrjefoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7a49nYSwh1QwBBmx7BcunKjPZKgOoeqRgDYuUlrQAtwZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/p5EFimp5o000oWWfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oDl8SIVwx4HwEpnwbAIuiCqa5KgNdfoo00oG1aCuk9gE1kmZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8JID3D9882efoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oW89HFDwU9ekRVkneoOuyGqO5WjPoSsGwKIvUphEl4jZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/osVThj4r8zefoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7LspHIWkRpQkEpuwr0K63eqbpehOND7SQKCvR1rQQ0lZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pJQAim8loo00omufoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7Xl9nEXlk4Ex0EwlOxZv3OqOMCuPNStTgbY7B9mQV4gZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/88BY3m0qrmefoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oXq9ncZwxlRkhIwlLsKuCHwbcWuNIbo000oT1OOt0lgRwwtZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/opQChzt8oo00omOfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7S5pidEkBlQzEBmke0LuiemP5OnaNCtTweKu0gxEQ8tZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/opQH2T55qWCfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7S4pSURw0NWxktgwLEM7XejMJP0OoDo000oSlOO7EozRQx1Z90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/oJZSjjN7oo00oDafoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oS78HJGmB9XwxY3wroJuyamPJajP4P6TVDYthtgTgEiZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/opRY2joqoo00ojKfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7G59nMZwR9Tl0MwwroHvXWrOsfyNNfoo00oTAKDuRNmTl1yZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/ocUC3Wkpoo00oDGfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7XurCVDxUtTwUNul7kNvXX2apXwOoSvSwDZ6RszEQlwZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/oMVTjzkpoo00omGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7W7p3QVmENexUpunOsO53ynaZTwO9T5RwCIuRJrQloiZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/psEFjTtoo00oqzWfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000ofk8yEZwU9QzUAywL4L7HeqP5ChOID3RwWD6RNkEQ12Z90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/o5VUiTho000oqzGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oDupXIUxUlXwhcynekM7yCkasevb9P8TgXY7RhqRgoiZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><\/p><p>此次&ldquo;丰收中国之县里丰收&rdquo;融合传播活动产品形态包含中视频、短视频、图文、直播等四个板块，每个板块都创新设计了新的展示形式和窗口，活动详情请戳⬇️：<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/r5ZUizsuoo00oWqfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oLq93JDlExflkpmke0NuHOibMKlOIatRwfb6xNqQ1pwZ90mtAO0O0OO0O0O.jpg\"><\/p><p >中央广播电视总台农业农村节目中心<\/p><p >与你17相聚金秋<\/p><p >&ldquo;县里丰收&rdquo;，扬帆起航<\/p>","type_name":"娱乐八卦"},{"art_id":96966,"type_id":47,"type_id_1":2,"art_name":"周深一袭绿衣亮相央视丰晚彩排，疑解锁新身份","art_status":1,"art_letter":"Z","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/o5MAjmovoo00ojKfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7TlpyEXmEIEzUBgwrxdvX2hPZ70bdeoSQqP600wRlwhZ90mtAO0O0OO0O0O.jpg","art_blurb":"近日，有网友拍到周深出现在浙江嘉兴平湖，录制央视《2022年中国农民丰收节晚会》。这也是继七夕晚会、中秋节晚会后，周深于近期连续参与的第三台央视的重大晚会。有网友称：这是周深总台分深！网友路透照片从网","art_up":4592,"art_hits":4592,"art_hits_day":4238,"art_hits_month":446,"art_time":"2022-09-13 15:01:14","art_score":"2.0","art_content":"<p>近日，有网友拍到周深出现在浙江嘉兴平湖，录制央视《2022年中国农民丰收节晚会》。这也是继七夕晚会、中秋节晚会后，周深于近期连续参与的第三台央视的重大晚会。有网友称：这是周深总台分深！<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/o5MAjmovoo00ojKfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7TlpyEXmEIEzUBgwrxdvX2hPZ70bdeoSQqP600wRlwhZ90mtAO0O0OO0O0O.jpg\"><\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/ppNZjGkuoo00oWufoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oDq93VClEJUkEA3lO4L7HGrPZo000olbt34SwCKuUpnRQFxZ90mtAO0O0OO0O0O.jpg\"><\/p><p >网友路透照片<\/p><p>从网友发布的照片可以看出，周深身着一袭绿衣，乘着渔船出场，&ldquo;小桥、流水、人家&rdquo;的节目设置和自身气质适配度极高，节目整体仿佛置身于山水画中一样。<\/p><p>这应是周深首次参与三农主题的大型晚会，从各种现场花絮物料来看，周深演唱依托的就是当地乡村的真实景色，这也是央视丰收节晚会首次沉浸式实景演绎。而周深演唱的作品，也与这田园风光十分匹配。不得不说，在陶醉于田园之美的同时，周深的唱功也一如既往的优秀，在彩排时就能听到现场阵阵的欢呼声。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/9MVZ2W8p82afoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7Woo00o9iBBmE5Xx0Bvwutc6SHwO5f3NIPo000oSAXZv043QgolZ90mtAO0O0OO0O0O.jpg\"><\/p><p >彩排间隙，周深为现场小演员签名<\/p><p>被粉丝封为&ldquo;话唠&rdquo;的周深经常被问到什么时候当回主持人，不知今年的丰收节晚会上是否能如愿尝试一回&ldquo;新身份&rdquo;？<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/88dY3Twqoo00omqfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000obupCFDl08AwENnkb8L6nP1MZoo00owPtOoGlDYuRlqEV4mZ90mtAO0O0OO0O0O.jpg\"><\/p><p>说到周深和晚会的缘分，从央视春晚、跨年晚会、中秋晚会，以及夏日歌会等，每次他带来的作品都能频频见诸热搜，成为网友热议的话题之一。今年首次登上总台丰收节晚会的舞台，周深又会带来什么样的惊喜，9月23日晚间黄金档，CCTV-1、CCTV-17&ldquo;2022年中国农民丰收节晚会&rdquo;，一起期待一下！<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8slUjW17o000oDCfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7O49nwZwU8AkUo1nbhbuCShOpOnaYH6RlaNvk5rQlokZ90mtAO0O0OO0O0O.jpg\"><\/p>","type_name":"娱乐八卦"},{"art_id":96965,"type_id":47,"type_id_1":2,"art_name":"中秋佳节“一起听”，酷狗联合华纳音乐打开中秋线上团圆新模式","art_status":1,"art_letter":"Z","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/r5ZRiDp4oo00ojGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000ooo00ok9n0Ywh5QxRZikLFbvXKgbJD3P9Loo00oTguP7U4zQFwjZ90mtAO0O0OO0O0O.jpg","art_blurb":"中秋节,中国传统四大节日之一。在这个花好月圆的日子,酷狗联合华纳音乐,以歌之名,借歌传情,携手明星艺人送上中秋祝福,许众歌迷一场浪漫音乐之旅。古时,人们喜以诗描月圆之美,诉相思之苦,而现代则常以歌曲音","art_up":44,"art_hits":44,"art_hits_day":4073,"art_hits_month":2043,"art_time":"2022-09-13 15:01:19","art_score":"5.0","art_content":"<p>中秋节,中国传统四大节日之一。在这个花好月圆的日子,酷狗联合华纳音乐,以歌之名,借歌传情,携手明星艺人送上中秋祝福,许众歌迷一场浪漫音乐之旅。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/r5ZRiDp4oo00ojGfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000ooo00ok9n0Ywh5QxRZikLFbvXKgbJD3P9Loo00oTguP7U4zQFwjZ90mtAO0O0OO0O0O.jpg\"><\/p><p>古时,人们喜以诗描月圆之美,诉相思之苦,而现代则常以歌曲音乐,表述心中所见所想所思。所以今年的中秋节,酷狗音乐青春专区特意发起音乐活动&ldquo;以歌之名&rdquo;,携手华纳音乐的萧敬腾、杨千嬅、袁娅维、金志文等艺人,一起邀请乐迷,在这个特殊的节日里,伴着好音乐,共享月圆人圆的美好。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/98UDhzJoo00orGqfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oLvp3EQwhlfwEA1nboJ7nymOJf1bdGrGwGI7RhkTlpyZ90mtAO0O0OO0O0O.jpg\"><\/p><p>此次,酷狗音乐中秋特别活动&ldquo;以歌之名&rdquo;,联手的华纳音乐集团,是全球三大音乐公司之一,拥有超过200年历史,业务覆盖全球70多个国家。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/9JUA2W0rqWSfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oTtpXMZxksAlUBnwL8IuiejOsKhPob7TFOJvk9iEg8lZ90mtAO0O0OO0O0O.jpg\"><\/p><p>华纳音乐旗下拥有众多世界知名音乐厂牌与公司,拥有强大的全球明星资源及丰富音乐曲库。本次便特邀签约艺人&mdash;&mdash;萧敬腾、杨千嬅、袁娅维、金志文,作为活动联合发起人,推荐收听好音乐,温暖人心的《城里的月光》,情深意浓的《花好月圆夜》,醉人醉心的《月亮忘了KISMET》、《月亮上跳舞 EMO WHISKEY》、《月光 CIRCLE》以及摇滚燃爆的《月亮不走我得走》,中秋共赏好月与好乐!<\/p><p>9月8日至9月15日,在酷狗音乐App,搜索&ldquo;中秋&rdquo;或&ldquo;华纳&rdquo;,参与评论区互动,明星签名照等着你!<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/9JMD3DNoo00oo000o2afoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7TqpnxBxUxVlkE3kr1a7XWnO5aladXo000oRgCCv000QA4sZ90mtAO0O0OO0O0O.jpg\"><\/p><p>中秋团圆,但仍有很多人在异乡奋斗,无法与家人相聚,或是恋人相隔异地。那么,可以再酷狗音乐App&ldquo;一起听&rdquo;,分享你心里的那首歌,以歌之名,向TA表达自己的思念。<\/p><p>你只需要在歌曲播放页中选择&ldquo;一起听&rdquo;功能,创建你的专属听歌房间,便能将邀请链接通过微信或QQ发送邀请TA,实现同步听歌。若要更换歌曲,可像日常听歌那样切出播放界面,搜索歌曲或歌单直接播放,或点击已有的歌曲列表进行播放。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/ppRZiTkkoo00oWqfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7HooyAYkEJQzRBhlO0J7nP3a5SvatSsGFbeuB9gQg93Z90mtAO0O0OO0O0O.jpg\"><\/p><p>据了解,酷狗&ldquo;一起听&rdquo;功能在2015年12月申请了技术专利,从技术上,通过将音频发送至指定信息群组内的第一用户和至少一个第二用户,使得第一用户和至少一个第二用户能够同步播放目标音频,从而针对同一音频进行交流,从而达到一首歌曲一人分享,多人共享的效果,实现&ldquo;跨时空跨距离&rdquo;听歌,同时还能实时发送文字和语音消息!中秋&ldquo;线上团圆&rdquo;,共赴一场音乐之旅!<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/oMkC3jkso000oDGfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000oO79HMUw0pWkBVjxb1d6XyhPpamPtz9TwPY6h9iFVlxZ90mtAO0O0OO0O0O.jpg\"><\/p><p>现在上酷狗音乐搜索&ldquo;中秋&rdquo;或&ldquo;以歌之名&rdquo;,发现更多好音乐,与TA一起听吧!<\/p>","type_name":"娱乐八卦"},{"art_id":96964,"type_id":47,"type_id_1":2,"art_name":"中式浪漫强势出圈，优酷的民族自信“奇妙游”妙在哪里？","art_status":1,"art_letter":"Z","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/8sJTjDMuo000oTCfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000o7roiZBlUIEwkNmnb8KuyanPcCvOdD4G1fc7E4xQF0hZ90mtAO0O0OO0O0O.jpg","art_blurb":"中秋佳节，月圆人团圆，代表着自古以来中国人对于团圆的美好景愿。优酷联合河南卫视打造《中秋奇妙游》，共同呈现跨越三千年的中秋故事。晚会以&ldquo;团圆&rdquo;为核心，细数友情、亲情、爱情、家国","art_up":1703,"art_hits":1703,"art_hits_day":3078,"art_hits_month":309,"art_time":"2022-09-13 15:01:25","art_score":"4.0","art_content":"<p>中秋佳节，月圆人团圆，代表着自古以来中国人对于团圆的美好景愿。优酷联合河南卫视打造《中秋奇妙游》，共同呈现跨越三千年的中秋故事。晚会以&ldquo;团圆&rdquo;为核心，细数友情、亲情、爱情、家国情等传统情结，为&ldquo;团圆&rdquo;含义扩容的同时，更融合展现多种曲艺、习俗元素，深厚的民族文化底蕴自然流露，勾勒出了一副属于中华民族特有的中式浪漫画卷。<\/p><p>中秋不仅分食月饼，&ldquo;团圆&rdquo;含义多元扩容<\/p><p>《中秋奇妙游》由极兔速递冠名，优酷与河南卫视联合打造，以嫦娥玉兔的神话故事为晚会主线，将月亮的阴晴圆缺和人间的悲欢离合、望月抒怀的浪漫畅想与热气奔腾生活百态有机结合。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8sJTjDMuo000oTCfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Yo000o7roiZBlUIEwkNmnb8KuyanPcCvOdD4G1fc7E4xQF0hZ90mtAO0O0OO0O0O.jpg\"><\/p><p>晚会中，《烟火人间》将中国曲艺元素和清明上河图融进节目里，用中国独特的艺术形式展现节日的氛围。《好久没见》，复原了一场穿越古今的&ldquo;中秋夜宴&rdquo;，展现古代传统宴席中&ldquo;蘸甲&rdquo;&ldquo;抛盘&rdquo;&ldquo;对诗&rdquo;&ldquo;行礼&rdquo;等习俗，趣味与文化感并重。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/p8cHjzksoo00omKfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Y7LopH0VmE1SzUZvlrkOuiOjOsOmaNL9RlaJuUlqQVwhZ8c4tAO0O0OO0O0O.jpg\"><\/p><p>而《神都相逢》等精彩节目以现代化创意和技术，实现了历史时空中伟大人物与现世&ldquo;团圆&rdquo;的效果。《自古英雄出少年》则通过武术表演、鼓舞、诗词吟唱，讲述个人与家国更大层面的深厚情感。情景表演《我欲乘风》更以&ldquo;飞天梦&rdquo;为主题，让&ldquo;飞天先驱&rdquo;明朝士大夫万户与如今航天员的跨时空对话，展现了中华民族虽重视家国&ldquo;团圆&rdquo;却并未被困住脚步，而是从古至今都更向往广袤的星空，不懈探索着世界的多种融合与交流的可能性，是更大意义的&ldquo;团圆&rdquo;追求。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/9MEH3Tpo000o8jGfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oTvo3REkkhWzUZjwuxd7HDya8XyOdKtSAHf6hI3QAgsZ90mtAO0O0OO0O0O.jpg\"><\/p><p>&ldquo;网剧+网综&rdquo;表达形式，多元融合诠释文化时代意义<\/p><p>此次《2022中秋奇妙游》，摒弃了过往传统的主持串场方式，而是以嫦娥月兔的神话为主线，由嫦娥倾听中秋人间许愿为契机，揭开一个个&ldquo;故事&rdquo;，带出不同主题的节目。在此过程中，优酷基于自身互联网视频内容平台&ldquo;网剧+网综&rdquo;资源，将时下大众熟悉的文化作品融入晚会当中，更具时代感知。<\/p><p>此前提到的《好久不见》节目，由优酷自制综艺《中国潮音》冠军裁缝铺乐队演唱，年轻化的rap讲述着千百年前的节俗，令人耳目一新记忆深刻。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pMBV2z18r2qfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Yo000o6793FFmEMAwUtmne4P7XKnMZoo00o3b9KrSlGD6klrQlshZ90mtAO0O0OO0O0O.jpg\"><\/p><p>同时，优酷还以合制形式，打造了具有平台特色的《团圆时刻》，将包含了友情、爱情、亲情等多种大众朴实情感的独播剧片段混剪，凭剧寄情，激发不同年龄、不同圈层大众对于中秋更多维的感知。<\/p><p>在混剪中，有《天雷一部之春花秋月》中分食月饼的传统节俗介绍，也有《传家》中乱世守护阖家齐整团圆的坚持与感动，还有《小敏家》里中年爱情特有的平稳与坚忍，更有《请叫我总监》中情侣与家长团聚过节的普通家庭缩影。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/psECiGgkqzGfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7XvoiJClUNTzEMylbpb6iemMZajadGrHwLevB8wR14kZ8c4tAO0O0OO0O0O.jpg\"><\/p><p>此外还有《胆小鬼》里的动人友谊、《爱情公寓》里经典的求婚场景、《致勇敢的你》里幸福的婚礼镜头、《今生有你》里一家三口出游的快乐合影&hellip;&hellip;片段串联，优酷将千百年来团聚对中国人的不同意义呈现在荧幕之上，传递出独特的中式浪漫。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/pZQFh20rrjCfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7W493cXk01XwBBhwOtdvXKiOJelO4T4TwePuh4xT1stZ90mtAO0O0OO0O0O.jpg\"><\/p><p>优酷特约合作伙伴梅见青梅酒，也以共创的穿越古诗歌曲节目，体现东方青梅酒的古雅文化国潮情怀，在酸甜微醺中去寻求与那些美好情感的&ldquo;好久没见&rdquo;。<\/p><p>持续探索文化融合创新，优酷立足激发民族自信<\/p><p>国风元素的兴起，传统文化和现代的结合，&ldquo;2022中秋奇妙游&rdquo;塑造的浪漫让原本的意义再度升级，传统的节日被更深层的了解，同时也有了更深层次的美誉浪漫。优酷一直在不断努力深耕中华传统文化意义，展现中华文化的魅力，激发民族文化的自信。<\/p><p>今年以来，优酷与河南卫视联手先后推出了清明、端午、七夕、中秋系列奇妙游晚会，除了唯美的文化审美与丰富的文化内涵外，优酷始终在寻找将传统与现实连接的方式。例如在《七夕奇妙游》晚会中，优酷结合晚会创新的&ldquo;女性力量&rdquo;主题，以合制形式在节目中,巧妙将时代女性精神与传统女性力量融合,演绎出了一番不一样的七夕味道。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/rslZ3jh5o000omafoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2k3Y7Koo00ooXUUwkoCwhI0lLkJ73agPJOvbtz4GASKuU4zFQpyZ8c4tAO0O0OO0O0O.jpg\"><\/p><p>晚会中，合制节目《隐形的翅膀》在厦门六中合唱团清新、轻快的阿卡贝拉中,跳伞运动员邢雅萍、拳击冠军张伟丽等时代女性代表纷纷亮相，配合而后的优酷《励志时刻》, 《幸福到万家》中不断抗争自强自立的何幸福、《请叫我总监》中坚持自我梦想的柠檬、《冰糖炖雪梨》中永不服输的雪梨、《女心理师》中温暖而强大的贺顿，与现实人物相辉映,当代女性的力量与勇气,也以更多维的形式展现在大众面前。<\/p><p>古今交汇，展现出的都是中华民族传承不变的闪光品质，优酷不断与河南卫视等优秀的合作伙伴共同探索，将传统文化融入时代语境，让大众能更亲切、自然地感知到民族文化优深厚的底蕴，希望能以此激发不同人群的民族自豪感。未来，优酷仍将在发扬民族文化方面持续深耕，通过更多的影视作品、跨界合作，放大民族时代之美。<\/p>","type_name":"娱乐八卦"},{"art_id":96963,"type_id":47,"type_id_1":2,"art_name":"李浩天《亲爱的生命》央八热播 凭借细腻演技获好评","art_status":1,"art_letter":"L","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/95RUjmokrGWfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oHo83wTlU1VzEFgkrsG7CaqbMXwPtSsR1GL7EpgT1knZ90mtAO0O0OO0O0O.jpg","art_blurb":"由王迎、毋辉辉执导，爱奇艺、恒顿传媒联合出品的都市医疗情感剧《亲爱的生命》已正式登陆CCTV-8黄金档，并在爱奇艺、腾讯视频同步播出。该剧由宋茜、王晓晨、尹昉等主演，嘉慕传媒签约演员李浩天在剧中出演了","art_up":2862,"art_hits":2862,"art_hits_day":4949,"art_hits_month":1707,"art_time":"2022-09-13 15:01:29","art_score":"7.0","art_content":"<img src=\"https:\/\/img.yparse.com\/upload\/95RUjmokrGWfoj3ZiPjutpRAj1vqeOyGbuQUyHBG00M3ofW7W2k3Yo000oHo83wTlU1VzEFgkrsG7CaqbMXwPtSsR1GL7EpgT1knZ90mtAO0O0OO0O0O.jpg\"><p>由王迎、毋辉辉执导，爱奇艺、恒顿传媒联合出品的都市医疗情感剧《亲爱的生命》已正式登陆CCTV-8黄金档，并在爱奇艺、腾讯视频同步播出。该剧由宋茜、王晓晨、尹昉等主演，嘉慕传媒签约演员李浩天在剧中出演了严峰，凭借细腻的表演收获了不少观众的好评。<\/p><p>近年来，女性生育话题一直是社会关注的热点，《亲爱的生命》正是将故事背景放在了迎接新生命的妇产科。该剧主要讲述了盛济医院妇产科住院医师的医生生活，在各种各样的病例中见识到千奇百怪的人生百态，从而也引发了人们对生命的全新思考。<\/p><p>由演员李浩天饰演的严峰，于前几日播出的剧情中出场，严峰和妻子是一对结婚多年的恩爱夫妻，俩人一直想要一个孩子。但由于夫妻二人基因不合，妻子几番怀孕孩子都遭夭折，严峰虽迫于母亲的压力，但依然决定不再让妻子受这个苦。李浩天通过细腻的眼神和表演，展现出了严峰在儿子和丈夫的两个身份中内心的纠结和焦灼，从行动中表达出对妻子的爱和丈夫的责任担当，给观众留下深刻印象。<\/p><p>毕业于中央戏剧学院表演系的李浩天，不仅是一位演员，同时也是一位导演和戏剧工作者。他与著名戏剧导演林兆华合作十余年，参演多部话剧作品，在话剧《三姐妹&middot;等待戈多》中李浩天一人饰两角，富有感染力的表演更是收获了观众好口碑。作为导演的他，于年初执导的首档聚焦中国神话故事的创演型文化节目《少年的奇幻世界》在中央广播电视总台播出，播出后收获好评如潮。<\/p><p>据悉，李浩天待播电影《谢文东》《最好的相遇》《海面上漂过的奖杯》，由《阳光之下》原班人马打造的电视剧《不期而至》也会陆续跟观众见面，怀着真诚与热爱，李浩天在表演的道路上不断前进，让我们一起期待他未来更多精彩表现。<\/p>","type_name":"娱乐八卦"},{"art_id":96962,"type_id":47,"type_id_1":2,"art_name":"这些破圈的神级音乐现场,你看过几个?","art_status":1,"art_letter":"Z","art_from":"Administrator","art_author":"Administrator","art_class":"未知","art_pic":"https:\/\/img.yparse.com\/upload\/8pJVijIooo00oWOfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7Lp9nVGkUwEwkoykb1ZunCmP5T1OoH4HVOJ7k9lEl4nZ90mtAO0O0OO0O0O.jpg","art_blurb":"前段时间天王刘德华的线上演唱会大家都看了吗?9月3日晚,刘德华的一场线上演唱会把大家的记忆瞬间拉回到属于自己的青春岁月。《笨小孩》《冰雨》《男人哭吧哭吧不是罪》接连几首经典歌曲让&ldquo;刘德华线","art_up":3565,"art_hits":3565,"art_hits_day":3125,"art_hits_month":2572,"art_time":"2022-09-13 15:01:32","art_score":"4.0","art_content":"<p>前段时间天王刘德华的线上演唱会大家都看了吗?<\/p><p>9月3日晚,刘德华的一场线上演唱会把大家的记忆瞬间拉回到属于自己的青春岁月。《笨小孩》《冰雨》《男人哭吧哭吧不是罪》接连几首经典歌曲让&ldquo;刘德华线上演唱会&rdquo;等关键词迅速冲上微博热搜。据官方数据统计,该演唱会总计观看人次破3.5亿。演唱会后,不少网友也纷纷在社交媒体发文缅怀自己的青春岁月。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8pJVijIooo00oWOfoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7Lp9nVGkUwEwkoykb1ZunCmP5T1OoH4HVOJ7k9lEl4nZ90mtAO0O0OO0O0O.jpg\"><\/p><p>如果刘德华并不是你的青春,那前段时间崔健、罗大佑,周杰伦和李健的线上演唱会,总有那么几首歌会引起你的共鸣。对于大部分网友来说,这些让自己感动的演唱会现场不仅仅是因为歌曲本身,它更多代表的是对一个时代的怀念。一代人有一代人的偶像,一代人有一代人的青春回忆。80后的青春有刘德华,90后的青春是周杰伦,00后是TFboys,10后的青春又会是谁呢?<\/p><p>在人工智能快速发展的当下,10后的青春可能会有时代少年团,又或是某位虚拟歌手?其实不止00后、10后,AI虚拟艺人早已突破大家刻板印象中的二次元形象,成为受众群广泛的多栖艺人。从初代虚拟艺人洛天依到超人气偶像虚拟团体A-SOUL,技能拉满的虚拟艺人们凭借各不相同的人设和特点受到了众多粉丝的喜爱。<\/p><p>新晋出道的虚拟歌手Luya就是其中之一,这位由科大讯飞旗下厂牌&ldquo;讯飞音乐&rdquo;推出的虚拟歌手出道仅仅十多天就已经发布了三首音乐作品,包括爆火歌曲的翻唱与自己的原唱歌曲,其中Luya近日与二十四伎乐合作重新演绎歌曲《雾里》更是一经发布就收到来自网友的大量&ldquo;彩虹屁&rdquo;。<\/p><p ><img src=\"https:\/\/img.yparse.com\/upload\/8pRWjWloo00orGefoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2k3Y7Hr8CAUk0JWxUBklLoG5iCrOZo000ohadD2HVSIuU5iQA8mZ90mtAO0O0OO0O0O.jpg\"><\/p><p>在Luya个人社交媒体&ldquo;LUYA不吃卤鸭&rdquo;发布的歌曲视频里,穿着一身汉服的Luya,用有着超高辨识度的&ldquo;Luya专属音色&rdquo;和知名乐团二十四伎乐合作演绎歌曲。虚拟歌手加上国风民乐的搭配,产生别样的火花,让网友直呼&ldquo;yyds&rdquo;。<\/p><p>而此次Luya与二十四伎乐合作的歌曲《雾里》也是讯飞音乐出品的爆款歌曲之一。讯飞音乐相比于传统音乐公司&ldquo;主推歌手&rdquo;的方式,致力于打造爆款音乐,用&ldquo;以歌带人&rdquo;的方式完成歌曲和歌手的破圈。加上大热的歌曲《雾里》,讯飞音乐出品的歌曲总播放量突破400亿次,70多首歌曲播放过亿,QQ音乐、酷狗音乐及网易云音乐热歌榜上榜歌曲已达数百首。除了大量的出圈歌曲,讯飞音乐也有了自己的艺人经纪业务,签约了游鸿明、简弘亦等艺人,与此同时还培养了一批新生代歌手如姚六一、霄磊等。<\/p><p>作为讯飞音乐推出的首位虚拟歌手,Luya虽然出道仅仅十几天,却已经演绎了三首歌曲,这样的成绩不禁让人对Luya的星途充满期待。此外,据讯飞音乐官方消息,讯飞音乐与AKB48一起打造的全新小分队Holidaygirls也发布了属于自己的音乐作品,期待Luya和Holidaygirls未来给大家带来更多的惊喜!我们一起拭目以待吧～<\/p>","type_name":"娱乐八卦"}]}
    """.trimIndent()
}