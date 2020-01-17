package org.fhi360.lamis.mobile.cparp.webservice;

import android.content.Context;
import android.util.Log;

import org.fhi360.lamis.mobile.cparp.dao.AccountDAO;
import org.fhi360.lamis.mobile.cparp.dao.DevolveDAO;
import org.fhi360.lamis.mobile.cparp.dao.FacilityDAO;
import org.fhi360.lamis.mobile.cparp.dao.PatientDAO;
import org.fhi360.lamis.mobile.cparp.dao.RegimenDAO;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.Scrambler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by aalozie on 2/13/2017.
 */

public class WebserviceResponseHandler {
    private Context context;

    public WebserviceResponseHandler(Context context) {
        this.context = context;
    }

    public void parseResult(String result) {
        try {
            Log.v("Webservice handler", "Response...."+result);
            if(!result.isEmpty()) {
                JSONObject response = new JSONObject(result);
                AccountDAO accountDAO = new AccountDAO(context);
                JSONArray accounts = response.optJSONArray("accounts");
                if(accounts != null) {
                    for (int i = 0; i < accounts.length(); i++) {
                        Log.v("Webservice handler", "Accounts....");
                        JSONObject account = accounts.optJSONObject(i);
                        int communitypharmId = account.getInt("communitypharm_id");
                        String pharmacy = account.getString("pharmacy");
                        String address = account.getString("address");
                        String phone = account.getString("phone");
                        String email = account.getString("email");
                        String pin = account.getString("pin");

                        int id = accountDAO.getId(communitypharmId);
                        if(id != 0) {
                            accountDAO.update(id, communitypharmId, pharmacy, address, phone, email, pin);
                        }
                        else {
                            accountDAO.save(communitypharmId, pharmacy, address, phone, email, pin);
                        }
                    }
                }

                FacilityDAO facilityDAO = new FacilityDAO(context);
                JSONArray facilities = response.optJSONArray("facilities");
                if(facilities != null) {
                    for (int i = 0; i < facilities.length(); i++) {
                        Log.v("Webservice handler", "Facility....");
                        JSONObject facility = facilities.optJSONObject(i);
                        int facilityId = facility.getInt("facility_id");
                        String state = facility.getString("state");
                        String lga = facility.getString("lga");
                        String name = facility.getString("name");

                        int id = facilityDAO.getId(facilityId);
                        if(id != 0) {
                            facilityDAO.update(id, facilityId, state, lga, name);
                        }
                        else {
                            facilityDAO.save(facilityId, state, lga, name);
                        }
                    }
                }

                RegimenDAO regimenDAO = new RegimenDAO(context);
                JSONArray regimens = response.optJSONArray("regimens");
                if(regimens != null) {
                    for (int i = 0; i < regimens.length(); i++) {
                        JSONObject regimen = regimens.optJSONObject(i);
                        int regimenId = regimen.getInt("regimen_id");
                        String description = regimen.getString("regimen");
                        int regimentypeId = regimen.getInt("regimentype_id");
                        String regimentype = regimen.getString("regimentype");

                        int id = regimenDAO.getId(regimenId);
                        if(id != 0) {
                            regimenDAO.update(id, regimenId, description, regimentypeId, regimentype);
                        }
                        else {
                            regimenDAO.save(regimenId, description, regimentypeId, regimentype);
                        }
                    }
                }

                PatientDAO patientDAO = new PatientDAO(context);
                JSONArray patients = response.optJSONArray("patients");
                Scrambler scrambler = new Scrambler();
                if(patients != null) {
                    for (int i = 0; i < patients.length(); i++) {
                        JSONObject patient = patients.optJSONObject(i);
                        int facilityId = patient.getInt("facility_id");
                        int patientId = patient.getInt("patient_id");
                        String hospitalNum = patient.getString("hospital_num");
                        String uniqueId = patient.getString("unique_id");
                        String surname = patient.getString("surname");
                        surname = scrambler.unscrambleCharacters(surname);
                        String otherNames = patient.getString("other_names");
                        otherNames = scrambler.unscrambleCharacters(otherNames);
                        String gender = patient.getString("gender");
                        Date dateBirth = patient.getString("date_birth").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("date_birth"), "yyyy-MM-dd");
                        String address = patient.getString("address");
                        address = scrambler.unscrambleCharacters(address);
                        String phone = patient.getString("phone");
                        phone = scrambler.unscrambleNumbers(phone);
                        Date dateStarted = DateUtil.parseStringToDate(patient.getString("date_started"), "yyyy-MM-dd");
                        String regimentype = patient.getString("regimentype");
                        String regimen = patient.getString("regimen");
                        String lastClinicStage = patient.getString("last_clinic_stage");
                        Double lastViralLoad = patient.get("last_viral_load").toString().isEmpty()? 0.0 : patient.getDouble("last_viral_load");
                        Date dateLastViralLoad = patient.getString("date_last_viral_load").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("date_last_viral_load"), "yyyy-MM-dd");
                        Date viralLoadDueDate = patient.getString("viral_load_due_date").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("viral_load_due_date"), "yyyy-MM-dd");
                        String viralLoadType = patient.getString("viral_load_type");
                        Double lastCd4 = patient.get("last_cd4").toString().isEmpty()? 0.0 : patient.getDouble("last_cd4");
                        Date dateLastCd4 = patient.getString("date_last_cd4").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("date_last_cd4"), "yyyy-MM-dd");
                        Date dateLastRefill = patient.getString("date_last_refill").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("date_last_refill"), "yyyy-MM-dd");
                        Date dateNextRefill = patient.getString("date_next_refill").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("date_next_refill"), "yyyy-MM-dd");
                        Date dateLastClinic = patient.getString("date_last_clinic").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("date_last_clinic"), "yyyy-MM-dd");
                        Date dateNextClinic = patient.getString("date_next_clinic").isEmpty()? null : DateUtil.parseStringToDate(patient.getString("date_next_clinic"), "yyyy-MM-dd");
                        String lastRefillSetting = patient.getString("last_refill_setting");

                        int id = patientDAO.getId(facilityId, patientId);
                        if(id != 0) {
                            patientDAO.update(id, facilityId, patientId, hospitalNum, uniqueId, surname, otherNames, gender, dateBirth, address, phone, dateStarted, regimentype, regimen, lastClinicStage,
                                    lastViralLoad, dateLastViralLoad, viralLoadDueDate, viralLoadType, lastCd4, dateLastCd4, dateLastRefill, dateNextRefill, dateLastClinic, dateNextClinic, lastRefillSetting);
                        }
                        else {
                            patientDAO.save(facilityId, patientId, hospitalNum, uniqueId, surname, otherNames, gender, dateBirth, address, phone, dateStarted, regimentype, regimen, lastClinicStage,
                                    lastViralLoad, dateLastViralLoad, viralLoadDueDate, viralLoadType, lastCd4, dateLastCd4, dateLastRefill, dateNextRefill, dateLastClinic, dateNextClinic, lastRefillSetting);
                        }
                    }
                }

                DevolveDAO devolveDAO = new DevolveDAO(context);
                JSONArray devolves = response.optJSONArray("devolves");
                if(devolves != null) {
                    for (int i = 0; i < devolves.length(); i++) {
                        JSONObject devolve = devolves.optJSONObject(i);
                        int facilityId = devolve.getInt("facility_id");
                        int patientId = devolve.getInt("patient_id");
                        Date dateDevolved = DateUtil.parseStringToDate(devolve.getString("date_devolved"), "yyyy-MM-dd");
                        String viralLoadAssessed = devolve.getString("viral_load_assessed");
                        Double lastViralLoad = devolve.get("last_viral_load").toString().isEmpty()? 0.0 : devolve.getDouble("last_viral_load");
                        Date dateLastViralLoad = devolve.getString("date_last_viral_load").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_last_viral_load"), "yyyy-MM-dd");
                        String cd4Assessed = devolve.getString("cd4_assessed");
                        Double lastCd4 = devolve.get("last_cd4").toString().isEmpty()? 0.0 : devolve.getDouble("last_cd4");
                        Date dateLastCd4 = devolve.getString("date_last_cd4").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_last_cd4"), "yyyy-MM-dd");
                        String lastClinicStage = devolve.getString("last_clinic_stage");
                        Date dateLastClinicStage = devolve.getString("date_last_clinic_stage").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_last_clinic_stage"), "yyyy-MM-dd");
                        String arvDispensed = devolve.getString("arv_dispensed");
                        String regimentype = devolve.getString("regimentype");
                        String regimen = devolve.getString("regimen");
                        Date dateLastRefill = devolve.getString("date_last_refill").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_last_refill"), "yyyy-MM-dd");
                        Date dateNextRefill = devolve.getString("date_next_refill").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_next_refill"), "yyyy-MM-dd");
                        Date dateLastClinic = devolve.getString("date_last_clinic").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_last_clinic"), "yyyy-MM-dd");
                        Date dateNextClinic = devolve.getString("date_next_clinic").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_next_clinic"), "yyyy-MM-dd");
                        String notes = devolve.getString("notes");;
                        Date dateDiscontinued = devolve.getString("date_discontinued").isEmpty()? null : DateUtil.parseStringToDate(devolve.getString("date_discontinued"), "yyyy-MM-dd");
                        String reasonDiscontinued = devolve.getString("reason_discontinued");;

                        int id = devolveDAO.getId(facilityId, patientId, dateDevolved);
                        if(id != 0) {
                            devolveDAO.update(id, facilityId, patientId, dateDevolved, viralLoadAssessed, lastViralLoad, dateLastViralLoad, cd4Assessed, lastCd4, dateLastCd4, lastClinicStage, dateLastClinicStage,
                                    arvDispensed, regimentype, regimen, dateLastRefill, dateNextRefill, dateLastClinic, dateNextClinic, notes, dateDiscontinued, reasonDiscontinued);
                        }
                        else {
                            devolveDAO.save(facilityId, patientId, dateDevolved, viralLoadAssessed, lastViralLoad, dateLastViralLoad, cd4Assessed, lastCd4, dateLastCd4, lastClinicStage, dateLastClinicStage,
                                    arvDispensed, regimentype, regimen, dateLastRefill, dateNextRefill, dateLastClinic, dateNextClinic, notes, dateDiscontinued, reasonDiscontinued);
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
