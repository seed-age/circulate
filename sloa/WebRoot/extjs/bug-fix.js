// TODO FIXME
Ext.override(Ext.ux.Spinner, {
			oldFn : Ext.ux.Spinner.triggerBlur,
			triggerBlur : function() {
				try {
					this.oldFn.call(this);
				} catch (e) {// 仅仅是为了不报JS错误。
					// alert(e);
				}
			}
		});

// Ext.override(Ext.form.Field,)

Ext.override(Ext.form.DisplayField, {

			getValue : function() {
				return this.value ? this.value : '';
			},
			setValue : function(v) {
				this.value = v;
				this.setRawValue(this.formatValue(v));
				return this;
			},
			formatValue : function(v) {
				if (this.dateFormat && Ext.isDate(v)) {
					return v.dateFormat(this.dateFormat);
				}
				if (this.numberFormat && typeof v == 'number') {
					return Ext.util.Format.number(v, this.numberFormat);
				}
				return v;
			}
		});
