package xxy.com.gridleader.model;

/**
 * Created by Betty Li on 2018/4/26.
 */

public class ResidentResultModel {
    private Long id;
    private String id_number;
    private String genderStr;
    private String contact_information;
    private String roomNumber;
    private String relationship;
    private String id_typeStr;
    private String communityName;
    private String name;
    private String gardenName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public String getContact_information() {
        return contact_information;
    }

    public void setContact_information(String contact_information) {
        this.contact_information = contact_information;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getId_typeStr() {
        return id_typeStr;
    }

    public void setId_typeStr(String id_typeStr) {
        this.id_typeStr = id_typeStr;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }
}
