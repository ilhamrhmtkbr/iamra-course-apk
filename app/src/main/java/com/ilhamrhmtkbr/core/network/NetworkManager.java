package com.ilhamrhmtkbr.core.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkManager {
    private ConnectivityManager connectivityManager;
    private MutableLiveData<Boolean> isConnectedLiveData;
    private MutableLiveData<NetworkType> networkTypeLiveData;
    private ConnectivityManager.NetworkCallback networkCallback;

    public enum NetworkType {
        WIFI,
        CELLULAR,
        ETHERNET,
        VPN,
        NONE
    }

    public enum NetworkSpeed {
        SLOW,      // < 150 kbps
        MODERATE,  // 150 kbps - 550 kbps
        FAST,      // 550 kbps - 2 Mbps
        VERY_FAST  // > 2 Mbps
    }

    public NetworkManager(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.isConnectedLiveData = new MutableLiveData<>();
        this.networkTypeLiveData = new MutableLiveData<>();
        initializeNetworkCallback();
    }

    // Initialize network callback for real-time monitoring
    private void initializeNetworkCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    updateNetworkStatus();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    updateNetworkStatus();
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    updateNetworkStatus();
                }
            };
        }
    }

    // Start monitoring network changes
    public void startNetworkMonitoring() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && networkCallback != null) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
        }
        updateNetworkStatus();
    }

    // Stop monitoring network changes
    public void stopNetworkMonitoring() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && networkCallback != null) {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            } catch (Exception e) {
                // Handle exception if callback was not registered
            }
        }
    }

    // Update network status and notify observers
    private void updateNetworkStatus() {
        boolean isConnected = isConnected();
        NetworkType networkType = getNetworkType();

        isConnectedLiveData.postValue(isConnected);
        networkTypeLiveData.postValue(networkType);
    }

    // Check if device is connected to internet
    public boolean isConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    // Get current network type
    public NetworkType getNetworkType() {
        if (!isConnected()) {
            return NetworkType.NONE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return NetworkType.WIFI;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return NetworkType.CELLULAR;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return NetworkType.ETHERNET;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        return NetworkType.VPN;
                    }
                }
            }
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                int type = networkInfo.getType();
                switch (type) {
                    case ConnectivityManager.TYPE_WIFI:
                        return NetworkType.WIFI;
                    case ConnectivityManager.TYPE_MOBILE:
                        return NetworkType.CELLULAR;
                    case ConnectivityManager.TYPE_ETHERNET:
                        return NetworkType.ETHERNET;
                    default:
                        return NetworkType.NONE;
                }
            }
        }
        return NetworkType.NONE;
    }

    // Check if connected via WiFi
    public boolean isWifiConnected() {
        return getNetworkType() == NetworkType.WIFI;
    }

    // Check if connected via mobile data
    public boolean isMobileDataConnected() {
        return getNetworkType() == NetworkType.CELLULAR;
    }

    // Get network speed estimation
    public NetworkSpeed getNetworkSpeed() {
        if (!isConnected()) {
            return NetworkSpeed.SLOW;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                if (capabilities != null) {
                    int downstreamBandwidth = capabilities.getLinkDownstreamBandwidthKbps();

                    if (downstreamBandwidth > 2000) {
                        return NetworkSpeed.VERY_FAST;
                    } else if (downstreamBandwidth > 550) {
                        return NetworkSpeed.FAST;
                    } else if (downstreamBandwidth > 150) {
                        return NetworkSpeed.MODERATE;
                    }
                }
            }
        } else {
            // For older versions, estimate based on network type
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int type = networkInfo.getType();
                int subtype = networkInfo.getSubtype();

                if (type == ConnectivityManager.TYPE_WIFI) {
                    return NetworkSpeed.FAST;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    switch (subtype) {
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NetworkSpeed.SLOW;
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NetworkSpeed.MODERATE;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NetworkSpeed.FAST;
                        default:
                            return NetworkSpeed.MODERATE;
                    }
                }
            }
        }

        return NetworkSpeed.SLOW;
    }

    // Get network type as string
    public String getNetworkTypeString() {
        NetworkType type = getNetworkType();
        switch (type) {
            case WIFI:
                return "WiFi";
            case CELLULAR:
                return "Mobile Data";
            case ETHERNET:
                return "Ethernet";
            case VPN:
                return "VPN";
            default:
                return "No Connection";
        }
    }

    // Get network speed as string
    public String getNetworkSpeedString() {
        NetworkSpeed speed = getNetworkSpeed();
        switch (speed) {
            case VERY_FAST:
                return "Sangat Cepat";
            case FAST:
                return "Cepat";
            case MODERATE:
                return "Sedang";
            default:
                return "Lambat";
        }
    }

    // Check if network is metered (limited data)
    public boolean isNetworkMetered() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return connectivityManager.isActiveNetworkMetered();
        } else {
            return isMobileDataConnected();
        }
    }

    // Get LiveData for connection status
    public LiveData<Boolean> getConnectionLiveData() {
        return isConnectedLiveData;
    }

    // Get LiveData for network type
    public LiveData<NetworkType> getNetworkTypeLiveData() {
        return networkTypeLiveData;
    }

    // Check if suitable for downloading large files
    public boolean isSuitableForDownload() {
        return isConnected() &&
                (isWifiConnected() ||
                        (isMobileDataConnected() && getNetworkSpeed() != NetworkSpeed.SLOW));
    }

    // Check if suitable for video streaming
    public boolean isSuitableForVideoStreaming() {
        return isConnected() && getNetworkSpeed() != NetworkSpeed.SLOW;
    }

    // Get detailed network info
    public String getDetailedNetworkInfo() {
        if (!isConnected()) {
            return "Tidak ada koneksi internet";
        }

        StringBuilder info = new StringBuilder();
        info.append("Tipe: ").append(getNetworkTypeString()).append("\n");
        info.append("Kecepatan: ").append(getNetworkSpeedString()).append("\n");
        info.append("Terbatas: ").append(isNetworkMetered() ? "Ya" : "Tidak");

        return info.toString();
    }
}