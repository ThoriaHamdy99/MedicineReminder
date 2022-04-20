package com.example.medicinereminder.AddEditMedicine.presenter;

import android.content.Context;

import com.example.medicinereminder.AddEditMedicine.view.AddAndEditMedicationInterface;
import com.example.medicinereminder.model.MedicationPOJO;
import com.example.medicinereminder.model.NetworkValidation;
import com.example.medicinereminder.model.PatientDTO;
import com.example.medicinereminder.model.RequestDTO;
import com.example.medicinereminder.model.TrackerDTO;
import com.example.medicinereminder.repository.Repository;
import com.example.medicinereminder.services.network.NetworkDelegate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.List;


public class AddMedicationPresenter implements AddMedicationPresenterInterface, NetworkDelegate {
    private Repository repository;
    private AddAndEditMedicationInterface view;
    private Context context;

    public AddMedicationPresenter(AddAndEditMedicationInterface view, Context context) {
        this.repository = Repository.getInstance(this,context);
        this.view = view;
        this.context = context;
    }

    @Override
    public void updateToDatabase(MedicationPOJO medication) {
        repository.updateMedications(medication);
        checkUpdateToFirebase(medication);
    }

    @Override
    public void addToDatabase(MedicationPOJO medication) {
        repository.insertMedication(medication);
        checkUpdateToFirebase(medication);
    }

    private void checkUpdateToFirebase(MedicationPOJO medication){
        String email = NetworkValidation.checkShared(context);
        if(!email.equals("null")){
            repository.updatePatientMedicationList(email,medication);
        }
    }

    @Override
    public void onSuccess() {
        view.onSuccess();

    }

    @Override
    public void onFailure(Task<AuthResult> task) {

    }

    @Override
    public void onFailure(String errorMessage) {
        view.onFailure();
    }




    @Override
    public void onSuccessReturn(String userName) {

    }

    @Override
    public void onSuccessRequest(List<RequestDTO> requestDTOS) {

    }

    @Override
    public void onSuccessTracker(List<TrackerDTO> trackerDTOS) {

    }

    @Override
    public void onSuccess(boolean response) {

    }

    @Override
    public void onSuccessPatient(List<PatientDTO> patientDTOS) {

    }

    @Override
    public void isUserExist(boolean existance) {

    }

    @Override
    public void onUpdateMedicationFromFirebase(List<MedicationPOJO> medications) {

    }

    @Override
    public void onSuccessReturnMedicationList(List<MedicationPOJO> medicationPOJOList) {

    }
}
