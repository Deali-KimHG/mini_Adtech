package net.deali.intern.infrastructure.repository;

import net.deali.intern.domain.Advertisement;
import net.deali.intern.presentation.dto.AdvertisementResponse;

import java.util.List;

public interface AdvertisementRepositoryCustom {
    List<AdvertisementResponse> select10Advertisement(List<Advertisement> advertisementList);
}
