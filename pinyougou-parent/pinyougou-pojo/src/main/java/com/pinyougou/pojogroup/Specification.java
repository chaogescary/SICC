package com.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

/**
 * 由于规格和规格选项的联动性，为了操作上和逻辑上的连续性，设置Specification大对象
 *
 */
public class Specification implements Serializable {

	private TbSpecification specification;							//规格
	private List<TbSpecificationOption> specificationOptionList;	//规格选项
	
	public TbSpecification getSpecification() {
		return specification;
	}
	public void setSpecification(TbSpecification specification) {
		this.specification = specification;
	}
	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}
	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}	

}
