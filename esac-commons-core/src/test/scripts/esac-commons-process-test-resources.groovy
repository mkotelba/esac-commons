import com.esacinc.commons.utils.EsacStringUtils

EsacStringUtils.tokenize(((String) this.binding.getVariable("dirs"))).each{
    ant.mkdir(dir: new File(it))
}
