package org.game.service;

import org.game.dao.GameDao;
import org.game.dao.RoundDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public class ServiceLayerImplCopy extends ServiceLayerImpl{

    @Autowired
    public ServiceLayerImplCopy(GameDao gameDao, RoundDao roundDao) {
        super(gameDao, roundDao);
    }
}
