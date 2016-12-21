#!/usr/bin/env bash
cp $BUDDYBUILD_SECURE_FILES/google-services.json app/src/prod/google-services.json
cp $BUDDYBUILD_SECURE_FILES/google-services-qa.json app/src/qa/google-services.json
cp $BUDDYBUILD_SECURE_FILES/google-services-qa.json app/src/mock/google-services.json
