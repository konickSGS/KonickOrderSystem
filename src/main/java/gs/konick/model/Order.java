package gs.konick.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {
    private long id;
    private long userId;
    private Status status;
    private int total;
    private Date createDate;
    private List<Map<SaleUnit, Integer>> saleUnitsAndCount;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public int getTotal() {
        return total;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public List<Map<SaleUnit, Integer>> getSaleUnitsAndCount() {
        return saleUnitsAndCount;
    }

    public void setSaleUnitsAndCount(List<Map<SaleUnit, Integer>> saleUnitsAndCount) {
        this.saleUnitsAndCount = saleUnitsAndCount;
    }

    public static class Builder {
        private final Order receipt = new Order();

        public Builder setId(long id) {
            receipt.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            receipt.userId = userId;
            return this;
        }

        public Builder setStatusId(long statusId) {
            receipt.status = Status.getStatusById(statusId);
            return this;
        }

        public Builder setTotal(int total) {
            receipt.total = total;
            return this;
        }

        public Builder setCreateDate(Date createDate) {
            receipt.createDate = createDate;
            return this;
        }

        public Order build() {
            return receipt;
        }
    }
}
