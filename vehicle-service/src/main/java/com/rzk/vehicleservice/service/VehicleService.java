package com.rzk.vehicleservice.service;

import com.rzk.vehicleservice.exceptions.EntityAlreadyExistsException;
import com.rzk.vehicleservice.exceptions.EntityDoesNotExistException;
import com.rzk.vehicleservice.model.Vehicle;
import com.rzk.vehicleservice.model.VehicleImage;
import com.rzk.vehicleservice.model.VehicleStatusHistory;
import com.rzk.vehicleservice.repository.VehicleImageRepository;
import com.rzk.vehicleservice.repository.VehicleRepository;
import com.rzk.vehicleservice.repository.VehicleStatusHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vr;
    private final VehicleStatusHistoryRepository hr;
    private final VehicleImageRepository vir;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<VehicleImage> getImagesForVehicle(Long vehicleId) {
        return vir.findByVehicleId(vehicleId);
    }

    public VehicleImage uploadImage(Long vehicleId, MultipartFile file) throws IOException {
        Vehicle vehicle = vr.findById(vehicleId)
                .orElseThrow(() -> new EntityDoesNotExistException("Vehicle with id " + vehicleId + " not found"));

        Files.createDirectories(Paths.get(uploadDir));

        String original = file.getOriginalFilename();
        String extension = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf("."))
                : "";
        String fileName = UUID.randomUUID() + extension;

        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());

        boolean noImagesYet = vir.findByVehicleId(vehicleId).isEmpty();

        VehicleImage image = new VehicleImage();
        image.setVehicle(vehicle);
        image.setImageUrl("/uploads/vehicles/" + fileName);
        image.setIsMain(noImagesYet); // prva slika automatski postaje glavna
        image.setUploadedAt(Instant.now());

        return vir.save(image);
    }

    public void deleteImage(Long imageId) {
        VehicleImage image = vir.findById(imageId)
                .orElseThrow(() -> new EntityDoesNotExistException("Image with id " + imageId + " not found"));

        try {
            String fileName = Paths.get(image.getImageUrl()).getFileName().toString();
            Files.deleteIfExists(Paths.get(uploadDir, fileName));
        } catch (IOException e) {
            // slika se briše iz baze i ako fizičko brisanje ne uspe
        }

        boolean wasMain = image.getIsMain();
        Long vehicleId = image.getVehicle().getId();
        vir.delete(image);

        if (wasMain) {
            List<VehicleImage> remaining = vir.findByVehicleId(vehicleId);
            if (!remaining.isEmpty()) {
                VehicleImage newMain = remaining.get(0);
                newMain.setIsMain(true);
                vir.save(newMain);
            }
        }
    }

    public Vehicle save(Vehicle vehicle){
        vr.findByBrandAndModelAndVehicleType(vehicle.getBrand(), vehicle.getModel(), vehicle.getVehicleType())
                .ifPresent(v -> {
                    throw new EntityAlreadyExistsException(
                            "Vehicle with brand " + vehicle.getBrand() + ", model " + vehicle.getModel() +
                                    " and type " + vehicle.getVehicleType().getId() + " already exists"
                    );
                });
        Vehicle savedVehicle = vr.save(vehicle);

        VehicleStatusHistory history = new VehicleStatusHistory();
        history.setVehicle(savedVehicle);
        history.setStatus(savedVehicle.getCurrentStatus());
        history.setChangedAt(LocalDateTime.now());
        hr.save(history);
        return savedVehicle;
    }

    public List<Vehicle> getAvailableVehicles(){
        return vr.findByCurrentStatus("AVAILABLE");
    }

    public Vehicle geVehicleById(Long id){
        return vr.findById(id).orElseThrow(() -> new EntityDoesNotExistException("Vehicle with id " + id + " not found"));
    }

    public Vehicle updateStatus(Long id, String status){
        Vehicle vehicle = vr.findById(id).orElseThrow(()-> new EntityDoesNotExistException("Vehicle with id " + id + " not found"));
        if(!status.equals("AVAILABLE") && !status.equals("RENTED")){
            throw new IllegalArgumentException("Status must be either AVAILABLE or RENTED");
        }
        vehicle.setCurrentStatus(status);
        Vehicle updatedVehicle = vr.save(vehicle);

        VehicleStatusHistory history = new VehicleStatusHistory();
        history.setVehicle(updatedVehicle);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        hr.save(history);
        return updatedVehicle;
    }

    public List<VehicleStatusHistory> getStatusHistory(Long vehicleId){
        return hr.findByVehicleIdOrderByChangedAtDesc(vehicleId);
    }

    public List<Vehicle> getAll() {
        return vr.findAll();
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = vr.findById(id).orElseThrow(() -> new EntityDoesNotExistException("Vehicle with id " + id + " not found"));
        vr.delete(vehicle);
    }
}
