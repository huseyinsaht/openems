import { Environment } from "../app/shared/type/environment";
import { DefaultTypes } from '../app/shared/service/defaulttypes';

class OpenemsBackendDevEnvironment extends Environment {
  public readonly production = false;
  public readonly url = "wss://localhost:443/openems-backend-ui2";
  public readonly backend: DefaultTypes.Backend = "OpenEMS Backend"
  public debugMode = true;
}

export const environment = new OpenemsBackendDevEnvironment();
