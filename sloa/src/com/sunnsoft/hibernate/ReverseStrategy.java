package com.sunnsoft.hibernate;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.ForeignKey;
import org.springframework.util.Assert;

import java.util.List;

@SuppressWarnings("unchecked")
public class ReverseStrategy extends DelegatingReverseEngineeringStrategy {

	public ReverseStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	
	/**
	 * 可根据特定规则转换表名和列名
	 */
	@Override
	public String columnToPropertyName(TableIdentifier table, String column) {
		return super.columnToPropertyName(table, column);
	}


	/**
	 * 可根据特定规则排除特定的表
	 */
	@Override
	public boolean excludeTable(TableIdentifier ti) {
		return super.excludeTable(ti);
	}


	@Override
	public String tableToClassName(TableIdentifier tableIdentifier) {
		String tableClassName = super.tableToClassName(tableIdentifier);
		if (tableIdentifier.getName().toUpperCase().endsWith("_TBL")) {
			// 为了避免数据库表名使用特殊关键字导致Hibernate生成的SQL失效，每个表名都有TBL三个字母做结尾。
			return tableClassName.substring(0, tableClassName.length() - 3);
		}
		if(tableIdentifier.getName().toUpperCase().startsWith("T_")) {
			//有些表是以T_开头，因此需要兼容这部分表的反向工程。
			String name = tableClassName.substring(0, tableClassName.lastIndexOf(".")) + "." + tableClassName.substring(tableClassName.lastIndexOf(".") + 2, tableClassName.length());
			return name;
		}
		// 为了兼容其他系统引入的表，不已_TBL结尾的表用全名。
		return tableClassName;
		
	}

	@Override
	public String foreignKeyToCollectionName(String keyname,
			TableIdentifier fromTable, List fromColumns,
			TableIdentifier referencedTable, List referencedColumns,
			boolean uniqueReference) {
		String collectionName = super.foreignKeyToCollectionName(keyname,
				fromTable, fromColumns, referencedTable, referencedColumns,
				uniqueReference);
		if (collectionName.endsWith("Tbls"))
			collectionName = collectionName.substring(0, collectionName
					.length() - 4)
					+ "s";
		return collectionName;
	}

	@Override
	public String foreignKeyToEntityName(String keyname,
			TableIdentifier fromTable, List fromColumnNames,
			TableIdentifier referencedTable, List referencedColumnNames,
			boolean uniqueReference) {
		String entityName = super.foreignKeyToEntityName(keyname, fromTable,
				fromColumnNames, referencedTable, referencedColumnNames,
				uniqueReference);
		if (entityName.endsWith("Tbl")) {
			entityName = entityName.substring(0, entityName.length() - 3);
		} else if (entityName.indexOf("TblBy") != -1) {
			entityName = entityName.substring(0, entityName.indexOf("TblBy"))
					+ entityName.substring(entityName.indexOf("TblBy") + 5);
		}
		return entityName;
	}

	@Override
	public boolean isOneToOne(ForeignKey foreignKey) {
		return super.isOneToOne(foreignKey);
	}

	@Override
	public void setSettings(ReverseEngineeringSettings settings) {
		super.setSettings(settings);
	}

	public static void main(String[] args){
		String tableClassName = "com.sunnsoft.extjs.db.vo.TBaseClass";
		String name = tableClassName.substring(0, tableClassName.lastIndexOf(".")) + "." + tableClassName.substring(tableClassName.lastIndexOf(".") + 2, tableClassName.length());
		System.out.println(name);
		Assert.isTrue("com.sunnsoft.extjs.db.vo.BaseClass".equals(name));
	}
}
